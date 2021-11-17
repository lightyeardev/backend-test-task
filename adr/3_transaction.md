# Transaction

## ER
![](https://github.com/bobsantos/backend-test-task/blob/main/adr/resource/3_transaction.svg?raw=true)

## Endpoints

##### POST /account/{id}/transfer

REQUEST:
```json
{
  "target": "8254329c-4202-11ec-81d3-0242ac130003",
  "amount": 100.0
}
```

RESPONSE:
```json
{
  "transactionId": "d98163dc-4202-11ec-81d3-0242ac130003",
  "account_id": "edcb6890-bfb3-435c-895e-8f3180abda00",
  "balance": 50.0,
  "createTime": "2021-10-25T23:55:52.034618Z"
}
```
