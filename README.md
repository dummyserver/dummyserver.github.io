## Dummy Server

A tool for mocking HTTP services inspired from [WireMock](https://github.com/tomakehurst/wiremock) 
and I have added this project to test my Java skills and see if I was able to implement such a server by myself :)

### Features
- Add dummy responses based on the request
  - method
    ```
    "request": {
        "method": "GET",
    }
    ```
  - path
    ```
    "request": {
        "path": "/v1/users"
    }
    ```
    ```
    "request": {
        "path": "/v1/users/(.+)/posts"
    }
    ```
  - queries
    ```
    "request": {
        "queries": {
            "user.id": "1234567890"
        }
    }
    ```
    ```
    "request": {
        "queries": {
            "user.id": {
                 "contains": "1234567890" 
            }
        }
    }
    ```
  - headers
    ```
    "request": {
        "headers": {
            "User-Id": "1234567890"
        }
    }
    ```
    ```
    "request": {
        "headers": {
            "User-Id": {
                 "contains": "1234567890" 
            }
        }
    }
    ```
- Add dummy responses on **Runtime** using the **POST /api/dummy-response** and **POST /api/dummy-response-list** APIs

### Run locally
```shell
mvn clean spring-boot:run
```

[Default configuration](src/main/resources/application.properties) used by the DummyServer
```properties
server.port = 9999
dummy.responses.file = dummy-responses.json
```

### APIs

The DummyServer exposes 2 APIs to add more dummy responses:
- **POST /api/dummy-response**
  
  We can use this API to add a **single** dummy response to the DummyServer
  
  Example: 
  
  ```json
   {
       "description": "reset external API response",
       "request": {
           "method": "GET",
           "path": "/v1/users"
       },
       "response": {
           "status": 200,
           "headers": {
               "Content-Type": "application/json"
           },
           "body": []
       }
   }
  ```
  
- **POST /api/dummy-response-list**

  We can use this API to add a **multiple** dummy responses to the DummyServer
  
  Example: 

  ```json
   [
     {
       "description": "reset external API response",
       "request": {
           "method": "GET",
           "path": "/v1/users"
       },
       "response": {
           "status": 200,
           "headers": {
               "Content-Type": "application/json"
           },
           "body": []
       }
    },
    {
        "description": "reset external API response",
        "request": {
            "method": "GET",
            "path": "/v1/users/(.+)/posts"
        },
        "response": {
            "status": 200,
            "headers": {
                "Content-Type": "application/json"
            },
            "body": []
        }
    }
  ]
  ```
