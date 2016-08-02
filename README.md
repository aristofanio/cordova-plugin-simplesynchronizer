# cordova-plugin-simplesynchronizer
SimpleSynchronizer allows an android app work in offline.


## How do:
* each request is mapped in database;
* the client make request in database (sqlite) before of the server;
* database is updated on constant interval.


## Install
```
cordova plugin add https://github.com/aristofanio/cordova-plugin-simplesynchronizer --save
```

## Architecture (overview)
<img src="https://raw.githubusercontent.com/aristofanio/cordova-plugin-simplesynchronizer/master/doc/overview.png" width="480px" alt="Architecture overview">

## Use
* navigator.ag.requestService.register : use to map requests;
* navigator.ag.requestService.request : use to request from database;
```javascript
    //first: map request
	var register = function(){
        navigator.ag.requestService.register('books', 'GET', "https://www.googleapis.com/books/v1/volumes?q=android", function(){
            window.alert('Register realized with success.')
        })
    }
    //secound: request 
    var request = function(){
        navigator.ag.requestService.request('books', function(r){
            document.getElementById("jsonresult").innerHTML = JSON.stringify(r);
        })
    }
```

In HTML:
```
<div class="app">
    <h1>Apache Cordova</h1>
    <button onclick="register()">register action</button>
    <button onclick="request()">request action</button>
    <div>
        <p id="jsonresult"></p>
    </div>
</div>
```


## Related projects:
* [core](https://github.com/aristofanio/simplesynchronizer-core)
* [sample](https://github.com/aristofanio/cordova-plugin-simplesynchronizer-sample)
