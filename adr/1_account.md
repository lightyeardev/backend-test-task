# Account

## ER:
![](https://github.com/lightyeardev/backend-test-task/blob/main/adr/resource/1_account.svg?raw=true)

## Endpoints:

##### POST /account

REQUEST:
```json
{
  "name": "Lightyear"
}
```

RESPONSE:
```json
{
  "id": "edcb6890-bfb3-435c-895e-8f3180abda00",
  "version": 0,
  "name": "Lightyear",
  "state": "ACTIVE",
  "createTime": "2021-10-25T23:55:52.034618Z",
  "lastModified": "2021-10-25T23:55:52.034620Z"
}
```

##### GET /account/{id}

RESPONSE:
```json
{
  "id": "edcb6890-bfb3-435c-895e-8f3180abda00",
  "version": 0,
  "name": "Lightyear",
  "state": "ACTIVE",
  "createTime": "2021-10-25T23:55:52.034618Z",
  "lastModified": "2021-10-25T23:55:52.034620Z"
}
```

##### GET /account

RESPONSE:
```json
[
  {
    "id": "edcb6890-bfb3-435c-895e-8f3180abda00",
    "version": 0,
    "name": "Lightyear",
    "state": "ACTIVE",
    "createTime": "2021-10-25T23:55:52.034618Z",
    "lastModified": "2021-10-25T23:55:52.034620Z"
  }
]
```
