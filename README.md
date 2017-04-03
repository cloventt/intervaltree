Interval Tree Implementation in Java
====================================

## 2017 Update

This project was initially created by Kevin Dolan in 2010, and has been forked and updated by David Palmer in 2017. 

I found myself needing a quick way to find the country code of an IP, given a list of IP blocks and associated country codes. Kevin's project suited this need perfectly, so I adapted it. I wanted to support IPv4 and IPv6 however, which presented an annoying inefficiency. Supporting IPv6 meant I needed to move from using `long` like Kevin to `BigInteger` (because IPv6 is represented by 128 bits). This worked fine, but because I'd hardcoded `BigInteger` in to the class and I was also using it for IPv4, there was massive memory overhead (an unsigned `Integer` would be heaps more memory efficient for IPv4).

The solution was to make the `IntervalTree` class accept any generic `Number` subclass for the intervals, and on the off-chance this is useful to anyone else I thought I'd throw it back up online. Plus the old library was written for Java 6, and I'm using Java 8.

Major improvements include:
 * use of generics so you can use any* arbitrary number type in your intervals (just specify a number type when you create the tree)
 * updated to use Java 1.8 (not tested on previous versions)
 * tests and maven integration
 
Major drawbacks include:
 * no support for primitives (added memory overhead)
 
(\*Disclaimer: I say any `Number`, but specifically it must also satisfy `Comparable<N super Number>`, which is most of them luckily)

## Original Disclaimer

This project is no longer maintained or supported. If you find this useful and would like to contribute or submit a pull request, I will just make you a contributor.

I wrote this back in 2010, when I was a Sophomore in Undergrad. I have not used it or updated it since then. I was an _okay_ programmer back then, but I've learned a lot since then so no guarantees on quality here. I'm dumping this here because I am taking down my old blog and a lot of people would download this from there.

Released under the [WTFPL](http://en.wikipedia.org/wiki/WTFPL).

## Original release notes from 2010

In a recent Java project, I found myself needing to store several intervals of time which I could access readily and efficiently.  I only needed to build the tree once, so a static data structure would work fine, but queries needed to be as efficient as possible.

I found a data structure that accomplishes just this, an interval tree.

It’s a simple enough data structure, but I couldn’t find any Java implementations for it online.

I then went to coding the data structure at the airport last week, and just finished unit testing it to convince myself everything was good to go.  I’m making it available if you are interested, because it’s really a waste of time to hand-code a well-known data structure.

It uses generic typing for the data object, but requires all the intervals to be expressed in terms of longs.  There are probably some obvious problems with catching programmer error.  For instance, if you search for an interval, but reverse start and end, I don’t know what will happen, nor do I care.

It is a static data structure, meaning it must be rebuilt anytime a change is made to the underlying data.  Rebuilds happen automatically if you try to make a query and it is out-of-sync, but you can build manually by calling .build() if you want to, and you can find out if it is currently in-sync by calling .inSync().

## Usage
The tree makes no effort to check that you are not mixing data types, so you should _always_ let the compiler know what you plan to store in it. Mixing data types for ranges is not supported, and may cause awkward `ClassCastException` errors, so proceed at your own risk.

You must also supply a constructor for your chosen Number sub-type that represents zero. This is a side-effect of using generics and type erasure. For most number types you can just pass a lambda that creates a new zero-value object into the constructor of the Interval Tree:

```
new IntervalTree<>( () -> Integer(0) );
new IntervalTree<>( () -> Double(0.0) );
new IntervalTree<>( () -> BigInteger.valueOf(0) );
new IntervalTree<>( () -> BigDecimal.valueOf(0.0) );
...etc...
```

To create an interval tree using ints for ranges and strings as data, simply, do this:

```
IntervalTree<Integer, String> tree = new IntervalTree<>(() -> 0);
```

Then you can add an interval like this:

```
tree.addInterval(5, 10, "This is the range of 5 to 10 inclusive");
```

Now you can query the tree like this:

```
tree.get(7);
```

This will return a `List` of data like this:

```
["This is the range of 5 to 10 inclusive"]
```

If you have overlapping ranges then `get()` will return multiple data values in the list, one for every range your query intersects. Its up to you to handle this list, including any the case where your value isn't in a range in the tree.

A valid range is any two different numbers, where the first number comes before the second on the number line.

More usage examples can be found in the `src/test/java/intervalTree` directory.

## Deployment
If you want the .jar and you have maven, simply run:

```
mvn install
```

from the project root. 

It's also hosted in the Maven Central Repo, so you can include it in your Maven project by adding

```
<dependency>
    <groupId>net.cloventt</groupId>
    <artifactId>intervalTree</artifactId>
    <version>2.0.3</version>
</dependency>
```

to your `pom.xml`. Then you can import it just like anything else:

```
import intervalTree.IntervalTree;
```

## License
For the updated version I'm relicensing this to Apache 2.0. The original WTFPL license is great but some organisations may not permit the use of software with an untested license like that.

Copyright 2017 David Palmer

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.