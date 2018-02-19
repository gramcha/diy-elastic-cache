# diy-elastic-cache
A simple implementation of distributed cache service. It will distribute the load to available cache systems using consistent hashing and allows you to add/remove cache system. It is developed as a spring boot application and uses the Jedis client for redis cache access.

#### How load is distributed across cache servers?
This service implements the consistent hashing algorithm to distribute load to available cache servers in the pool.

##### Consistent Hashing
The simplicity of consistent hashing is pretty mind-blowing. Here you have a number of nodes in a cluster of caches. How do you figure out where the data for a particular key goes in that cluster?

You apply a hash function to the key. That's it? Yeah, that's the whole deal of consistent hashing.

The same key will always return the same hash code (hopefully), so once you've figured out how you spread out a range of keys across the nodes available, you can always find the right node by looking at the hash code for a key.

Each cache node will be assigned a range of hash values to store the objects.

Nodes | Range of hash values
--- | --- 
Node 0 | 0000… ~ 3fff…
Node 1 | 3fff… ~ 7ffe…
Node 2 | 7fff… ~ bffd…
Node 3 | bffd… ~ ffff…


###### Mapping of objects to different nodes
Node will be picked from above range when we try to store object into cache based on hash value of that object.


| Object  | Hash value (hexadecimal)  | Node mapped to
|---|---|---
| Image 1  | b5e7d988cfdb78bc3be1a9c221a8f744  |   Node 2
| Image 2  | 943359f44dc87f6a16973c79827a038c  |   Node 2
| Image 3  | 1213f717f7f754f050d0246fb7d6c43b  |   Node 0
| Music 1  | 4b46f1381a53605fc0f93a93d55bf8be  |   Node 1
| Music 2  | ecb27b466c32a56730298e55bcace257  |   Node 3
| Music 3  | 508259dfec6b1544f4ad6e4d52964f59  |   Node 1
| Movie 1  | 69db47ace5f026310ab170b02ac8bc58  |   Node 1
| Movie 2  | c4abbd49974ba44c169c220dadbdac71  |   Node 3

![alt text](https://github.com/gramcha/diy-elastic-cache/blob/master/chash1.png)

Now if I added additional nodes, only thing that changes is each node will get a new range of hash values it is going to store. Each object’s hash value will still remain the same. Any objects whose hash value is within range of its current node will remain. For any other objects whose hash value is not within range of its current node will be mapped to another node; but that number of objects is very few using consistent hashing algorithm, compared to the basic hash function.

I’ll add another node and re-illustrate my point on the picture below:

![alt text](https://github.com/gramcha/diy-elastic-cache/blob/master/chash2.png)

Notice how only Movie 2 and Music 2 objects were mapped to my new node (node 4), and Image 1 had to be mapped to node 3.

##### Multiple Markers in Consistent Hashing Algorithm

First, let’s look at what the multiple markers do for us.

Remember in consistent hashing algorithm, each node has one big range of hash values to map the objects. Multiple markers helps to evenly distribute the objects into nodes, thus helping with the load balancing, but how?

Instead of having one big hash range for each node, multiple markers serve to split those large hash range into smaller chunks, and those smaller hash ranges will be assigned to different nodes in the server. How does that help?

Let’s say I have 20 objects I want to store, and I still have 4 nodes, each with different range of hash values of equal length. But what if out of those 20 objects, maybe 14 are mapped to node 0, and the rest are equally distributed to nodes 1, 2, and 3? This causes the ring to be unbalanced in weight, because node 0 holds much more hash values than the rest of the nodes. This is where the smaller hash ranges can help a lot with load balancing.

As mentioned earlier, consistent hashing algorithm uses multiple markers for the nodes to map several smaller ranges of hash values instead of one big range. This has two positive effects: First, if the new node was to be added, that new node will gain more objects from all other existing nodes in the server, instead of just a few objects from a neighboring node – this results in more and smaller hash ranges. Likewise, if one of the existing node was to be removed, all objects that node was holding onto will be evenly distributed to the other existing nodes – results in less and larger hash ranges. Second, by doing this, the overall distribution of objects will be fairly even, meaning the weight among different nodes will be very close to evenly distributed – helps with load balancing.

![alt text](https://github.com/gramcha/diy-elastic-cache/blob/master/chash3.png)

Picture above shows several objects close to each other in terms of its hash value are distributed among different segments of the different drives. Multiple markers splits 4 big hash ranges into several smaller hash ranges and assigns them into all other drives.



#### Endpoint for getting a cached item from distributed cache

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

#### Endpoint for setting an item in distributed cache

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
