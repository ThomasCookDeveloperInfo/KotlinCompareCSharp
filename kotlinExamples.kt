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
      // - Java code can also depend on Kotlin code
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
      // - Nullability
      // - Extension Functions
      // - Data Classes
      // - Thread Safe Singletons
      // - Immutability
      // - Lambdas
      // - Higher Order Functions

package com.thomascook.examples

// Public and closed by default
// No need to call a constructor if there is no state to init
// Minimizing state is something we attempt to do to make it
// easier to reason about and test our system
class KotlinExamplesBasics {
  // fun is the keyword for function
  // Types come after names and return type comes at the end
  fun babysFirstFunction(name: String) : String {
      return "Hello " + name // Semi-colons are optional
  }

  // Remove braces and just return a value
  // This is called an expression bodied function
  // The body of the function is a single expression
  fun myFirstSexyFunction(name: String) = "Haha $name that's ridiculous"

  // Here's another one...
  // In this example we use a when expression
  // This is similar to a switch in C#
  // Because it's an expression it must always evaluate to a value
  // So you must provide an else case
  fun dontLikeCricket(sport: String) = when (sport) {
     "cricket" -> "I don't like $sport"
     else -> "I like $sport"
  }

  // This will compile
  // The return type will be 'Any'
  // Any is the root of the Kotlin type heirarchy
  fun ambiguousTypeEval(sport: String) = when (sport) {
    "cricket" -> -1
    else -> "I like $sport"
  }

  // This will return a nullable string 'String?'
  // Because nullability is built into the language using the type system
  // Any type can have a corrosponding nullable variant
  // So the compiler can infer that the return type of this function is String?
  // Whereas there is no type (other than Any) that can hold an Int or a String
  fun anotherAmbiguousTypeEval(sport: String) = when (sport) {
    "cricket" -> null
    else -> "I like $sport"
  }

  // Kotlin won't compile if we attempt to call
  // Methods on an object that may be null at run time
  // This means effort is shifted from run time debugging to compile time
  fun nullability() : List<String> {
    // Let's assign the possibly null return value to a variable
    val couldBeNull = anotherAmbiguousTypeEval("cricket")

    // Now, if we try to call lines() on our possibly null Strring
    // Without using Kotlin null safety features our solution won't compile
    //return couldBeNull.lines()

    // Secondly we can use '!!' operator to tell the compiler that
    // We are sure this value cannot be null and to just go ahead and execute
    // If the value happens to be null we cause an NPE and get fired - so don't do this
    return couldBeNull!!.lines()

    // We have two options instead
    // First we can use '?' operator to only execute code following the '?'
    // if the value is not null at run time. If it is null, lines would be null
    // We will NOT get any NPE at run time with this method
    //return couldBeNull?.lines()
  }
}

// Here's an extension function
// No need to put it in a static class
// You can control it's scope with scope modifiers like any other function
fun String.wordCount() = this.split(' ').size

// Data classes are equivelant to POCO but the compiler generates
// equals(), hashCode(), toString() and copy() for us
// One question I have is - how does the compiler know the context
// That the class is used in? How does it avoid hash clashing?
data class IqTest(val amIStupid: Boolean)

// We can define extensions on any type:
fun IqTest.areTheyStupid() = when (this.amIStupid) {
    true -> "No"
    else -> "Yes"
}

// Singletons are notoriously tricky to set up in a multi-threaded program
// In Kotlin, the complexity of this problem is dealt with by the compiler
// And we can simply create thread safe singletons like so
// If you're adventurous you can look at the byte code generated from this...
object MySingleton {
  fun fireTheRockets() = "Everyone is dead now you monster"
}

class KotlinExamplesImmutables {
  // Immutability is great for multithreading/async code
  // It essentially lets us trade memory to completely sidestep synchronization issues
  // Kotlin supports both mutable and immutable properties
  // Var means mutable whereas val means immutable
  data class Risky(var mutable: Int, val immutable: Int)

  // Now if we fire two threads off and both are accessing shared memory
  // We may end up with a race condition or odd behaviour
  fun riskyThreads() {
    val risky = Risky(0, 0)
    val thread1 = Thread({
      risky.mutable = 1
      println(risky)
    })
    val thread2 = Thread({
      risky.mutable = 2
      println(risky)
    })
    thread1.start()
    thread2.start()
  }

  // A much better approach would be to have all the properties on the class
  // be immutable, then the only way to mutate the object would be to create a copy
  data class MuchBetter(val immutable1: Int, val immutable2: Int)

  // In either thread, we simply return a new instance and update any properties
  // Yes, this could be achieved by just not mutating mutable properties
  // However, it's prone to error and forgetfullness.
  // Using val enforces immutability on you at compile time
  fun nonRiskyThreads() {
    val muchBetter = MuchBetter(0, 0)
    val thread1 = Thread({
      val modified = MuchBetter(1, muchBetter.immutable2)
      println(modified)
    })
    val thread2 = Thread({
      val modified = MuchBetter(2, muchBetter.immutable2)
      println(modified)
    })
    thread1.start()
    thread2.start()
  }
}

class KotlinExamplesLambdas {
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

  // Kotlin supports lambdas (we've just seen them in action) we can define them and assign them to variables
  // The syntax (String) -> String means it's a function that takes a String and returns a String
  val greet : (String) -> String = { name -> "greetings $name" }
  val grumpyGreet : (String) -> String = { name -> "feck off $name" }

  // Lambdas are not executed until their result is required by an expression
  // We can now create a higher order function that greets a group of people
  // With a passed function that matches the function pattern (String) -> String
  fun greetGroup(greetFun: (String) -> String, people: Collection<String>) : Collection<String> =
    people.map {
      greetFun.invoke(it)
    }

  // We can, if we're starting to lose the plot, define the above greetGroup as a lambda
  // Now we can pass this round as arguments to other functions or invoke it from anywhere in the class
  // In Kotlin lambda everything in parenthesis before the '->' are the arguments
  // And the type after '->' is the return type. You completely ommit argument names in the function signature.
  // In the definition you can use arbitrary names. I believe Kotln allows you to do function currying and partial application
  // But that's way above my head at the moment.
  val greetGroup: ((String) -> String, Collection<String>) -> Collection<String> = {
    greetFun, people -> people.map { greetFun.invoke(it) }
  }

  // Now I could invoke the lambda above and pass greet or grumpyGreet as the lambdas first argument
  // Just like we did with greetGroup function...
  fun greetGroupByInvokingLambda() = greetGroup.invoke(this.greet, listOf("Tom", "Dick", "Harry"))
}
