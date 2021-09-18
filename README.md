# Coding Challenge - Data Engineering

## Task 1

Using Java or Scala programming language process given input files to retrieve listed deliverables.

### Input

You are given a snapshots of two databases, describing existing advertisements at the platform and fraudulent actions reported by customer service:

*ads.csv* – a CSV file with following columns

* Ad ID

* Time of creation of an Ad in EPOCH format

* Price of an Ad

*fraudulent.csv* – a CSV file with following columns

* Ad ID of an Ad that was detected to be fraudulent

* Date as to when the ad was detected to be fraudulent in DD-MM-YYYY format

### Deliverables

1. A list of Ads that are fraudulent and their creation time. Deliver a CSV file of Ad IDs and creation time (in DD-MM-YYYY format), e.g.:

```
123455,12-02-2015 
123456,12-02-2016 
....
```

2. Average price of all fraudulent ads. Deliver an average price printed on console. 
3. Average price per day of all fraudulent ads. Deliver a CSV file with prices aggregated per day (in DD-MM-YYYY format), e.g.:

```
12-03-2016, 21345.67 
13-03-2016, 12357.45 
...
```

4. *[ Extra achievement ]* Explain how your program could handle high data volumes, exceeding RAM size on a single machine. For this step an explanation is enough.

### General Hints

* Use standard Scala/Java libraries (extra achievement is an exception).

* No javadoc necessary. Self explaining code is sufficient.

* Check your algorithm complexity in terms of Big(O) notation and be prepared to
  explain it.
  
* Write well testable (and well tested) code.

* You do not need to build any API or network interface for this.

  

## Task 2

**In this task, no code is necessary.** You have to design a system that serves the *N* of last actions of each user at our site. The possible actions are “*View car*”, “*Search*”, “*Add car to the basket*” and “*Contact dealer*”, each action has some meta data, like model and color of viewed car. The service will be used as input by existing ML based models at *mobile.de* in order to predict the user behavior on our site. Based on the current *mobile.de* traffic, we can advise you to expect a lot of requests on it.

Make sure the system you designed is capable of support the following capabilities:

*  The ability to receive a request containing a user id, a type of event (“*View car*”, “*Search*”, “*Add car to the basket*” and “*Contact dealer*”) and an integer parameter *N*. The service must answer the last *N* actions done by this user. For example, the service should be able to answer what were the last 5 searches done by the user 12345.

*  The service response time must aim to be as fast as possible in order to serve
high traffic. However, you don't need to provide high consistency guarantees. It is Ok for most use cases to operate on eventually consistent data.

*  The system should scale so we can add additional resources without breaking modifications.

We would like you to present

* Design a solution that solves this problem and explain your technical choices. Imagine a scenario where you need to present your solution to other engineers in your team, who will have a lot of questions. Here you can build some slides to illustrate your ideas.
* And again, don’t code. This is about designing an architecture, not coding.
* Don’t forget to present ideas you’ve thought about but finally dismissed. The

same goes for potential problems.

### General Hints

* You can use any tools you want (RDBMS, NoSQL, Load balancers, Hadoop...). Please justify your technological choices, this is really a big plus if they are well chosen for the problem.
* You are allowed to set up reasonable constraints on service functionality such as TTLs, consistency delay, etc.
* Think about how the data is collected and used by the service, the data model to be used and the maintenance of this system.