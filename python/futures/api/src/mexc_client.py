# mexc_client.py
import time
import hmac
import hashlib
import json
import logging
from typing import Optional, Dict, Any, List, Union
from urllib.parse import urlencode

import requests


# 配置日志
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)


class MexcHttpClient:
    """MEXC API - """
    
    BASE_URL = "https://api.mexc.co"
    
    def __init__(self, api_key: Optional[str] = None, secret_key: Optional[str] = None):
        self.api_key = api_key
        self.secret_key = secret_key
        self.is_signed = api_key is not None and secret_key is not None
        
        self.session = requests.Session()
        self.session.headers.update({
            'Accept': 'application/json'
        })
        
    def _generate_signature(self, timestamp: str, params: Dict[str, Any]) -> str:
        sign_target = self._create_signature_string(timestamp, params)
        return self._sign(sign_target)
    
    def _sign(self, sign_target: str) -> str:
        logger.debug(f"Sign target: {sign_target}")
        mac = hmac.new(
            self.secret_key.encode('utf-8'),
            sign_target.encode('utf-8'),
            hashlib.sha256
        )
        return mac.hexdigest()
    
    def _create_signature_string(self, timestamp: str, params: Dict[str, Any]) -> str:
        string_builder = [self.api_key, timestamp]
        
        if params:
            # 对参数进行排序
            sorted_params = sorted(params.items())
            for key, value in sorted_params:
                string_builder.append(f"{key}={value}")
                string_builder.append("&")
            # 移除最后一个"&"
            if string_builder[-1] == "&":
                string_builder.pop()
                
        return "".join(string_builder)
    
    def _generate_signature_with_body(self, timestamp: str, body_raw: str) -> str:
        sign_target = self.api_key + timestamp + body_raw
        return self._sign(sign_target)
    
    def _build_url(self, endpoint: str, params: Optional[Dict[str, Any]] = None) -> str:
        url = f"{self.BASE_URL}{endpoint}"
        if params:
            query_string = urlencode(params)
            url = f"{url}?{query_string}"
        return url
    
    def _execute(self, request: requests.PreparedRequest) -> Dict:
        try:
            response = self.session.send(request)
            response_body = response.text
            
            if response.status_code != 200:
                logger.error(f"Request failed, HTTP status: {response.status_code}")
                logger.error(f"Response body: {response_body}")
                return None
            
            json_response = json.loads(response_body)
            if not json_response.get('success', False):
                logger.error(f"Business error, code: {json_response.get('code')}, msg: {json_response.get('message')}")
                
            return json_response
            
        except requests.exceptions.RequestException as e:
            logger.error(f"Request failed: {e}")
            raise
        except json.JSONDecodeError as e:
            logger.error(f"JSON decode failed: {e}")
            raise
    
    def get(self, endpoint: str, params: Optional[Dict[str, Any]] = None) -> Dict:
        url = self._build_url(endpoint, params)
        logger.debug(f"GET {url}")
        
        request = requests.Request(
            'GET',
            url,
            headers={'Accept': 'application/json'}
        ).prepare()
        
        return self._execute(request)
    
    def get_signed(self, endpoint: str, params: Optional[Dict[str, Any]] = None) -> Dict:
        if not self.is_signed:
            raise ValueError("API key and secret key required for private endpoints")
        
        if params is None:
            params = {}
            
        timestamp = str(int(time.time() * 1000))
        url = self._build_url(endpoint, params)
        
        request = requests.Request(
            'GET',
            url,
            headers={
                'Content-Type': 'application/json',
                'ApiKey': self.api_key,
                'Request-Time': timestamp,
                'Signature': self._generate_signature(timestamp, params)
            }
        ).prepare()
        
        return self._execute(request)
    
    def post_signed(self, endpoint: str, params: Optional[Dict[str, Any]] = None) -> Dict:
        if not self.is_signed:
            raise ValueError("API key and secret key required for private endpoints")
        
        if params is None:
            params = {}
            
        body_string = json.dumps(params)
        timestamp = str(int(time.time() * 1000))
        
        request = requests.Request(
            'POST',
            f"{self.BASE_URL}{endpoint}",
            headers={
                'Accept': 'application/json',
                'ApiKey': self.api_key,
                'Request-Time': timestamp,
                'Signature': self._generate_signature_with_body(timestamp, body_string)
            },
            json=params
        ).prepare()
        
        return self._execute(request)
    
    def post_signed_with_array_body(self, endpoint: str, data: List[Any]) -> Dict:
        if not self.is_signed:
            raise ValueError("API key and secret key required for private endpoints")
        
        body_string = json.dumps(data)
        timestamp = str(int(time.time() * 1000))
        
        request = requests.Request(
            'POST',
            f"{self.BASE_URL}{endpoint}",
            headers={
                'Accept': 'application/json',
                'ApiKey': self.api_key,
                'Request-Time': timestamp,
                'Signature': self._generate_signature_with_body(timestamp, body_string)
            },
            json=data
        ).prepare()
        
        return self._execute(request)