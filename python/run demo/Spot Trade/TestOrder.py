from python.spot import mexc_spot_v3
import json

class TestNewOrder:
    def __init__(self):
        self.trade = mexc_spot_v3.mexc_trade()

    def test_example1_all_params_via_body(self, params):
        """
        Example 1: All parameters sent via request body (Form/Text format)
        Uses the post_order_test_via_body method which sends params via request body
        """
        print("=== Example 1: All parameters via request body (Form/Text) ===")
        result = self.trade.post_order_test_via_body(params)
        return result

    def test_example2_all_params_via_query(self, params):
        """
        Example 2: All parameters sent via query string
        Uses the new post_order_test_query_only method
        """
        print("=== Example 2: All parameters via query string ===")
        result = self.trade.post_order_test(params)
        return result


def main():
    print("\n" + "="*80)
    print("Starting API Tests with Dynamic Timestamps")
    print("="*80)
    
    # Initialize test client
    test_client = TestNewOrder()
    
    symbol = "🤮🐰💗❤️🌞💤📖⛰️USDT"
    # Basic parameters
    all_params = {
        "symbol": symbol,
        "side": "BUY",
        "type": "LIMIT",
        "quantity": "1",
        "price": "11",
        "recvWindow": "5000"
    }
    
    try:
        # Example 1: All parameters sent via request body
        print("\n" + "="*60)
        print("Testing Example 1: All parameters via request body")
        print("="*60)
        result1 = test_client.test_example1_all_params_via_body(all_params.copy())
        print(f"Example 1 Result: {json.dumps(result1, indent=2)}")
    except Exception as e:
        print(f"Example 1 failed: {str(e)}")
    
    try:
        # Example 2: All parameters sent via query string
        print("\n" + "="*60)
        print("Testing Example 2: All parameters via query string")
        print("="*60)
        result2 = test_client.test_example2_all_params_via_query(all_params.copy())
        print(f"Example 2 Result: {json.dumps(result2, indent=2)}")
    except Exception as e:
        print(f"Example 2 failed: {str(e)}")
    


if __name__ == "__main__":
    main()
