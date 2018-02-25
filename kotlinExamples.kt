// Kotlin Brown Bag

//====== Overview ======//
  // What is Kotlin?
      // - It is (not) the second coming of Christ
      // - Statically typed
      // - Can be compiled to Java byte code OR Javascript (so could be used as alternative to Typescript)
      // - It runs on the Java Virtual Machine (JVM)
          // - Meaning the byte code is interpreted with Just in Time Compilation (JIT)
          //   similar to C# or any other language that run in a VM
      // - It's interoperable with Java which means it can be integrated with existing code base with ease
      // - It's arguably less verbose than Java and C#
      // - There isn't much (if anything) that Kotlin can do that Java/C# can't
          // - However, Kotlin makes it easier to take a more functional approach to solving problems (less boilerplate)
      // - Functions are first class citizens
      // - Lots of libraries (due to interop with Java)
      // - Immutability built in (great for async/multithreading)

  // What's coming up?
      // - General Syntactical Differences
      // - Expression Bodied Functions
      // - Type inference
      // - Extension Functions
      // - Nullability
      // - Thread Safe Singletons
      // - Data Classes
      // - Immutability
      // - Higher Order Functions
      // - Lambdas

package com.thomascook.examples

// Public and closed by default
// No need to call a constructor if there is no state to init
// Minimizing state is something we attempt to do to make it
// easier to reason about and test our system
class KotlinExamples {
  // fun is the keyword for function
  // Types come after names
  fun myFirstFunction(name: String) : String {
      return "Hello " + name // Semi-colons are optional
  }

  // Remove braces and just return a value
  // This is called an expression bodied function
  // The body of the function is a single expression
  fun myFirstSexyFunction(name: String) = "Haha $name that's ridiculous"

  // Here's another one...
  // In this example we use a when expression
  // This is similar to a switch in C#
  // A when expression must always provide an else branch
  fun dontLikeCricket(sport: String) = when (sport) {
     "cricket" -> "I don't like $sport"
     else -> "I like $sport"
  }

  // This will compile
  // The return type will be Any
  // Any is the root of the Kotlin type heirarchy
  fun ambigousTypeEval(sport: String) = when (sport) {
    "cricket" -> -1
    else -> "I like $sport"
  }

  // This will return a nullable string - String?
  // Because nullability is built into the language using the type system
  // Any type can have a corrosponding nullable variant
  // So the compiler can infer that the return type of this function is String?
  // Whereas there is no type (other than Any) that can hold an Int or a String
  fun anotherAmbigousTypeEval(sport: String) = when (sport) {
    "cricket" -> null
    else -> "I like $sport"
  }

  // Kotlin won't compile if we attempt to call
  // Methods on an object that may be null at run time
  // This means effort is shifted from run time debugging to compile time
  fun nullability() {
    // Let's assign the possibly null return value to a variable
    val couldBeNull = anotherAmbigousTypeEval("cricket")

    // Now, if we try to call lines() on our possibly null Strring
    // Without using Kotlin null safety features our solution won't compile
    // val wontCompile = couldBeNull.lines()
    // We have two options instead

    // First we can use '?' operator to only execute code following the '?'
    // if the value is not null at run time. If it is null, lines would be null
    // We will NOT get any NPE at run time with this method
    val lines = couldBeNull?.lines()

    // Secondly we can use '!!' operator to tell the compiler that
    // We are sure this value cannot be null and to just go ahead and execute
    // If the value happens to be null we cause an NPE and get fired - so don't do this
    val riskyLines = couldBeNull!!.lines()
  }

  // Immutability is great for multithreading/async code
  // It essentially lets us trade memory to completely sidestep synchronization issues
  // Kotlin supports both mutable and immutable properties
  // Var means mutable whereas val means immutable
  data class CantMakeMyMindUp(var mutable: Int, val immutable: Int)

  // Let's imagine these functions are called from different threads
  // But both are somehow passed the same instance of the class above
  // They could end up in a race condition where they are both trying to write
  // to the mutable property on the passed object
  fun threadA(sharedMemory: CantMakeMyMindUp) : CantMakeMyMindUp {
    sharedMemory.mutable = 0
    return sharedMemory
  }
  fun threadB(sharedMemory: CantMakeMyMindUp) : CantMakeMyMindUp {
    sharedMemory.mutable = 1
    return sharedMemory
  }

  // A much better approach would be to have all the properties on the class
  // be immutable, then the only way to mutate the object would be to create a copy
  data class MuchBetter(val immutable1: Int, val immutable2: Int)

  // In either thread, we simply return a new instance and update any properties
  // Yes, this could be achieved by just not mutating mutable properties
  // However, it's prone to error and forgetfullness.
  // Using val enforces immutability on you at compile time
  fun threadA(immutable: MuchBetter) : MuchBetter {
    return MuchBetter(0, immutable.immutable2)
  }
  fun threadB(immutable: MuchBetter) : MuchBetter {
    return MuchBetter(1, immutable.immutable2)
  }

  // Kotlin supports higher order functions
  // Here's a simple contrived example that applies a passed function to an argument
  fun myFirstHigherOrderFunction(applyThis: (String) -> Int, toThis: String) =
    applyThis.invoke(toThis)

  // Now we can pass some function to the above function using the power of lambdas
  fun application(someString: String) : Int {
    // Here we pass a function that maps any string to the number 1
    val always1 = myFirstHigherOrderFunction({ string -> 1 }, someString)

    // Here we pass a function that returns -1 when the string is "cricket" else 0
    val someNumber = myFirstHigherOrderFunction({ string -> when(string) {
        "cricket" -> -1
        else -> 0
    }}, someString)

    // Just add them together
    return always1 + someNumber
  }

  // A lambda is an expression and, as such, can be assigned to a variable
  // When we do this we must specify the function type
  // This is done using lambda notation
  // The example below is a function that takes a string and returns an int
  val lambdaVariable : (String) -> Int = { string -> 1 }

  // Here's another, more complex, one
  // This shows how you can nest function types to create complex programs
  // My current understanding is that any program can be expressed in terms of lambda
  // This lambda takes a string, and a function of String -> Int and returns an Int
  // The implementation simply invokes the inner lambda expression on the first parameter
  // It's all very mentally stimulating and elegant
  // but most of the time you won't be composing programs like this...
  val anotherOne : (String, ((String) -> Int)) -> Int = { string, lambda -> lambda.invoke(string) }

  // Kotlin has lots of higher order functions built into the standard library
  // Mainly they are to do with list computations, although there are several other useful ones
  fun mapOverList(names: Collection<String>) : Collection<String> {
     return names.map {
         "Greetings $it"
     }
  }

  // There are several useful generic higher order functions that work on all types
  // Let's create a simple type to demonstrate this
  class Bar(private val foo: String) {
    fun value() = "foo value: $foo"
    fun drawToScreen() = Unit
    fun cache() = true
  }

  // Let invokes a passed lambda on the object it was called on and returns the result
  // It saves us writing bar.println(value()); bar.drawToScreen(); bar.cache()
  fun letExample() {
    val bar = Bar("test")
    bar.let {
      println(it.value())
      it.drawToScreen()
      it.cache() // Implicit return line, return type of let block is Unit
    }
  }

  // Apply is like let, except it returns the object it was called on
  fun applyExample() : Bar {
    val bar = Bar("test")
    return bar.apply {
      println(value())
      drawToScreen()
      cache()
    }
  }

  // There are several other useful methods like this
  // And, due to lambda and higher order function support
  // We can define our own similar methods
}



// Here's an extension function
// No need to put it in a static class
// You can control it's scope with scope modifiers like any other function
fun String.wordCount() = this.split(' ').size

// We can define extension functions on any type
// Here's a really contrived example
data class Foo(val amIStupid: Boolean)
fun Foo.isHeStupid() = when (this.amIStupid) {
    true -> "No"
    else -> "Yes"
}

// We've just seen an example of a data class
// Data classes are equivelant to POCO but the compiler generates
// equals(), hashCode(), toString() and copy() for us
// One question I have is - how does the compiler know the context
// That the class is used in? How does it avoid hash clashing?


// Singletons are notoriously tricky to set up in a multi-threaded program
// In Kotlin, the complexity of this problem is dealt with by the compiler
// And we can simply create thread safe singletons like so
// If you're adventurous you can look at the byte code generated from this...
object MySingleton {
  fun fireTheRockets() = "Everyone is dead now"
}

fun main(args: Array<String>) {
  /* val kotlinExamples = KotlinExamples()

  println("Hello, what's your name?")
  val name = readLine() ?: "Tom"
  val myFirstFunction = kotlinExamples.myFirstFunction(name)
  println(myFirstFunction)

  println("Yea, but what's your nick name?")
  val sexyName = readLine() ?: "Dick"
  val myFirstSexyFunction = kotlinExamples.myFirstSexyFunction(sexyName)
  println(myFirstSexyFunction)

  println("What's your favourite sport?")
  val favouriteSport = readLine() ?: "cricket"
  val dontLikeCricket = kotlinExamples.dontLikeCricket("cricket")
  println(dontLikeCricket)

  println("Here's an example of a function that has different return types on different paths, so it returns Any")
  val ambigousTypeEval = kotlinExamples.ambigousTypeEval(favouriteSport)
  println(ambigousTypeEval.javaClass.name)

  println("Here's another example of a function that has different return types")
  println("This time, it returns a nullable type 'String?''")
  readLine()
  val anotherAmbigousTypeEval = kotlinExamples.anotherAmbigousTypeEval("test")
  println(anotherAmbigousTypeEval?.javaClass?.name ?: "String?")

  println("This is an extension function on the 'String' type that counts the words")
  println("Enter some words...")
  val words = readLine() ?: "Test test test test"
  val wordCount = words.wordCount()
  println("Word count of $words is $wordCount")

  println("Am I stupid?")
  val isStupid = readLine() as? Boolean ?: false
  val foo = Foo(amIStupid = isStupid)
  val amIStupid = foo.isHeStupid()
  val not = if (amIStupid == "Yes") "" else "not"
  println("$amIStupid you are $not stupid")

  println("If you press enter now, I'm going to call a singleton that launches a load of rockets..")
  readLine()
  val fireTheRockets = MySingleton.fireTheRockets()
  println(fireTheRockets + ", I told you so...")


  readLine()
  val appliedHigherOrderFunction = kotlinExamples.application("cricket")
  println(appliedHigherOrderFunction)

  readLine()
  val firstLambdaResult = kotlinExamples.lambdaVariable.invoke("It's going to be 1")
  println(firstLambdaResult)

  readLine()
  val secondLambdaResult = kotlinExamples.anotherOne.invoke("It's going to be 1", kotlinExamples.lambdaVariable)
  println(secondLambdaResult)

  readLine()
  val listOfGreetings = kotlinExamples.mapOverList(listOf("Tom", "Dick", "Harry"))
  listOfGreetings.map {
    println(it)
  }

  println()
  readLine() */
}
