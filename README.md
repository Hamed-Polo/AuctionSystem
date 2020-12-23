# AuctionSystem


I'm working with a real auction data online. The data will be in the form of an XML file. There may be extraneous data that we do not need in our analysis. We will selectively limit our object construction with information that we are interested in. To get started, visit the following websites:

Ebay Auction Data

Yahoo Auction Data

Here are shortcuts via the correspnding TinyURLs:

http://tinyurl.com/nbf5g2h - Ebay Auction Data
http://tinyurl.com/p7vub89 - Yahoo Auction Data


The libaray Big Data Jar will be used:

http://cs.berry.edu/big-data/bigdata.jar

I will also work with the idea of persistence. This means that our program should save all data from session to session. When we terminate a program, normally the data will be lost. We will preserve this data by using Serializable Java API and binary object files. All the classes should simply implement the java.io.Serializartion interface.

Example: The AuctionTable class contains information for all auctions saved in the electronic database. You would want to preserve this data, so you can load this data the next time you run your program. 
