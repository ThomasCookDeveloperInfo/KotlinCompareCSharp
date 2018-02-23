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
      // - It's less verbose than Java and C#
      // - There isn't much (if anything) that Kotlin can do that Java/C# can't
          // - However, Kotlin makes it easier to take a more functional approach to solving problems (less boilerplate)
      // - Functions are first class citizens
      // - Lots of libraries (due to interop with Java)
      // - Null built into the type system
      // - Immutability built in (great for async/multithreading)

  // What's coming up?
      // - General Syntactical Differences
      // - Nullability
      // - Expression Bodied Functions
      // - Named Function Parameters
      // - Extension Functions
      // - Lambdas
      // - Higher Order Functions
      // - Data Classes
      // - Destructuring
      // - Immutability
      // - Thread Safe Singletons



//====== General Syntactical Differences =======//
  // - No "new" keyword
  // - "fun" is used to define Functions
  // - Function sigs are different -> fun foo(x: Int) : Boolean { .. } vs public Boolean foo(Int x) { .. }
  // - Classes closed and public by default (no need to keep writing public everywhere)
  // - Semi colons are optional - so have fun arguing about that
  // - There are several language constructs that you might no have seen before
      // - when, ?:, ?, infix operaters etc. I'll talk about several of these shortly
  // - Names before types (i.e. C# -> Int foo = 1 vs Kotlin -> foo: Int = 1)



//====== Nullability =======//
  // - Much like C# 6.0 and onwards, Kotlin has the safe call operator '?.' for safe calling methods on nullable variables.
  //   However, unlike C#, Kotlin code will NOT compile if you attempt to ommit the '?.' on a nullable variable.
  // - This means effort is shifted from run time debugging to compile time type checking (and, at least for me, this is a great boon)
  // - You can force call a method on a possibly null variable using '!!' (not recommended)



//===== Expression Bodied Functions ========//
  // - Just like in C# you can have expression-bodied functions (i.e. public Int sum(Int a, Int b) => a + b).
  //   However, as far as I'm aware, C# doesn't allow you to have multi line expression-bodied functions (which Kotlin does).
  // - For instance:
  //
  // fun safeDivide(input: Int, denominator: Int) = when(denominator) {
  //     0 -> 0
  //     else -> input / denominator
  // }
  //
  //
  // - Or, from our Android codebase:
  //
  // override fun deleteRooms() = Observable.fromCallable {
  //            Realm.getDefaultInstance().use { realm ->
  //                val dbRooms = realm.where(DBRoom::class.java)
  //                        .findAll()
  //
  //                if (dbRooms.isNotEmpty()) {
  //                    RealmHelper.executeTransaction(realm, Callable<Unit> {
  //                        dbRooms.deleteAllFromRealm()
  //                    })
  //                    true
  //                } else {
  //                    false
  //                }
  //            }
  //        }.subscribeOn(AndroidRealmSchedulers.realmThread())



//====== Named Function Parameters =======//
  // - Like in C# 4.0 and onwards, Kotlin allows you to specify arguments by name, in any order.
  //   This is great when consuming code that has methods/constructors with massive argument lists.
  // - For instance:
  //
  // fun reallyObnoxiousFunction(debug: Boolean, drawCircle: Boolean, drawSquare: Boolean, animate: Boolean, clipping: Boolean) { .. }
  //
  // Hmmmm all I know is there are a load of boolean arguments
  // reallyObnoxiousFunction(true, false, true, true, true)
  //
  // Ok, it's a bit longer but it gives you LOADS more context.
  // reallyObnoxiousFunction(debug = true, drawCircle = true, drawSquare = false,
  //                         animate = true, clipping = true)



//======= Extension Functions ========//
  // - Also possible in C#, Kotlin let's you define extension methods on existing types.
  //   However, creating them in Kotlin requires far less boiler plate.
  //
  // - Take this C# code for example:
  //
  //   public static class MyExtensions
  //   {
  //       public static int WordCount(this String str)
  //       {
  //           return str.Split(new char[] { ', },
  //                            StringSplitOptions.RemoveEmptyEntries).Length;
  //       }
  //   }
  //
  // - The extension method must be a static method inside a static class.
  //   It's also arguably harder to read than the analogous Kotlin code:
  //
  //  fun String.wordCount() = this.split(',')
  //
  // - The Kotlin code above does not need to be inside a class, it can simply be
  //   defined in any package. In my opinion, it's a lot better than the C# analog.



//======= Lambdas =========//
  // - Again, also possible in C#, lambdas allow us to do a more declarative way of programming
  //   and opens up a whole slue of new ways to organize our code.




fun test(x: Int, y: Int) = x + y

fun main(args: Array<String>) {
  println(test(1, 2))
}
