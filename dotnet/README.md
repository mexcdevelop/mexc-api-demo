# MEXC SPOT API V3 signature examples for DotNET

[MEXC API document](https://mxcdevelop.github.io/apidocs/spot_v3_en/#signed) has the details of how to hash the signature. In this repo, we give the example script on how to do signature.

## How it works
In each language, the script will try to hash following string and should return same signature

```bash
# hashing string
timestamp=1652918401000
# and return
35f29f605c92b6f782b58bcd8513e48b903af50af0e7a9002f031a000b36de35

```

## Before you start to use our demo
- please enter your apiKey & apiSecret first.


## DotNET Compiling 
Build the solution. \
$ dotnet build

## DotNET Running
Run signature examples. \
$ dotnet run signature

Run spot examples. \
$ dotnet run spot
