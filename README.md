# diy-elastic-cache
A simple implementation of distribution cache service. It will distribute the load to available cache systems using consistent hashing and allows you to add/remove cache system. It is developed as a spring boot application and uses the Jedis client for radius cache access.


#### Endpoint for get cached item from distributed cache

http://localhost:8080/cache?key=somekey

##### GET
Service find the redis system based on the key and reterieve the same from that redis system.

**Sample Request**

    http://localhost:8080/cache?key=mailid.yt.com

where,
    key - request param represents the key stored in cache

**Sample Response**

    {
        "key": "mailid.yt.com",
        "value": "this my mail url",
        "status": "retrieved from 127.0.0.1:6379"
    }

#### Endpoint for set item in distributed cache

http://localhost:8080/cache

##### POST
Adds key and its value in distributed cache.

**Sample Request**

    {
    	"key":"mailid.yt.com",
    	"value":"this my mail url"
    }

where,
    key - key for the value
    value - value for the key

**Sample Response**

    {
        "key": "mailid.yt.com",
        "value": "this my mail url",
        "status": "cached in 127.0.0.1:6379"
    }


#### Endpoint for adding or removing cache system

http://localhost:8080/system

##### GET
Gets the list of redis cache system present in the distribution pool.

**Sample Response**

    [
        {
            "host": "127.0.0.1",
            "port": "6379",
            "portNumber": 6379
        },
        {
            "host": "127.0.0.1",
            "port": "6380",
            "portNumber": 6380
        }
    ]

##### POST
Adds cache server to distribution list.

**Sample Request**

    {
    	"host":"127.0.0.1",
    	"port":"6379"
    }

where,
host - ip address of the redis cache system
port - port number of the redis cache system

**Sample Response**

System 127.0.0.1:6379 added successfully.

##### DELETE
Removes cache server from distribution list.

**Sample Request**

    {
    	"host":"127.0.0.1",
    	"port":"6379"
    }

where,
host - ip address of the redis cache system
port - port number of the redis cache system

**Sample Response**

System 127.0.0.1:6379 removed successfully.
