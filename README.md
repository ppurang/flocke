# Flocke

Flocke is an **opinionated** decentralized, k-ordered id generation service in Scala. It doesn't do binary protocol (uses plain old and trusted HTTP) and doesn't do any json. It tries to be very lightweight. Intra JVM calls don't require any other libraries (unless you want batch support in that case the scalaz-stream and companions are all you need). 


## Many ways to get to a Flocke  (Intra-JVM)

``` scala 

    import org.purang.net.flocke.Flocke 
    
    //provide a network interface display name for the hardware address
    val eth0 : Either[String, Flocke] = Flocke("wlan0")
    
    //or provide a network interface hex code for the hardware address
    val hex: Either[String, Flocke]= Flocke.hex("54:42:49:97:a7:56")
    
    //or let us choose a mac address or if none is found then a random 15 digit long
    val chooseOne: Flocke = Flocke()
    
    //or if you are feeling lucky
    val provideALong: Flocke = Flocke(123789345876230l)
    
    //and then the next id is as far away as 
    val id = chooseOne.next
```


## Only one way to get to many flocken (Batch or Stream support)

``` scala 

    import org.purang.net.flocke.Flocke
    import org.purang.net.flocke.stream.FlockeStream._
    import scalaz._, Scalaz._
    
    Flocke.hex("54:42:49:97:a7:56").fold(
    
    s => println("can't do much without shiny new ids"),
    
    implicit f => {
      //might need 100 sometime soon
      val n = next(100)
    
      //ok now's the right time
      val ids: IndexedSeq[BigInt] = n.runLog.run
    
      //do something useful with the shiny new ids
      println(ids) //ok kinda lame
    })
```

## Well that's so 80s, so an http service?

Get one: 

```sh

    curl http://localhost:8080/flocke
    
    25863784966217230658086287638528
```

Get many:

```sh

    curl http://localhost:8080/flocke/4
    
    25863786539540032704773944967168,25863786539540032704773944967169,25863786539558479448847654518784,25863786539558479448847654518785
```

Rather have base/64?

```sh 

    curl -H"Accept: text/base64" http://localhost:8080/flocke
    
    MjU4NjM3ODc4OTk2NTUzNjY3NDc1MjY2MDQ3MTgwODA
```

Or,

```sh 

    curl -H"Accept: text/base64" http://localhost:8080/flocke/3
    
    MjU4NjM3OTAxNDUzODA0MzcwMjUwMDExMjgwMDE1MzY,MjU4NjM3OTAxNDUzODA0MzcwMjUwMDExMjgwMDE1Mzc,MjU4NjM3OTAxNDUzODA0MzcwMjUwMDExMjgwMDE1Mzg
```

No you don't need Json ;)

## Monitoring

todo


## Project Structure 

The project is divided  into 

1. **flocke-core**   : The main code for generating Ids
2. **flocke-stream** : The place for batch/stream support
3. **flocke-server** : An http4s based http service


# INSPIRATIONS

Inspirations:

- https://github.com/boundary/base64

- https://github.com/gideondk/vlok, flakepack

- https://github.com/marklister/base64

