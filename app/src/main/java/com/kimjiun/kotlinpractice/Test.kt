package com.kimjiun.kotlinpractice

fun main(){
    // varTest()
    // loopTest()

    //classTest1()
    //classTest2()
    //classTest3()
    //lamdaTest1()
    //lamdaTest2()
    nullTest()
}

fun nullTest(){
    val data: String? = null
    println("data len : ${data?.length ?: 0}")
}

// 타입 별칭
typealias MyInt = Int
typealias MyFunc = (Int, Int) -> Boolean

fun lamdaTest2(){
    val data1 : Int = 10
    val data2 : MyInt = 10

    val someFun: MyFunc = {no1: Int, no2:Int -> no1 > no2}
    val someFun2: MyFunc = {no1, no2 -> no1 > no2} // 매개변수 타입 생략 가능
    val someFun3 = {no1: Int, no2:Int -> no1 > no2} // 변수 타입 유추 가능
    // val someFun4 = {no1, no2 -> no1 > no2} // 에러
    println(someFun(10, 20))
    println(someFun(20, 10))

    // 고차함수 : 함수를 매개변수나 반환값으로 사용
    fun hofFun(arg: (Int) -> Boolean) : () -> String{
        val result = if(arg(10)){
            "valid"
        }
        else{
            "invalid"
        }
        return {"hofFun result : $result"}
    }

    val result = hofFun { no -> no > 0 }
    println(result)
    println(result())
}

fun lamdaTest1(){
    val sum = {no1: Int, no2:Int -> no1 + no2}
    println(sum(1, 2))

    val p = {-> println("function call")}
    val p2 = {println("function call")}
    p()
    p2()

    val some = {no:Int -> println(no)}
    val some2:(Int) -> Unit = { println(it)}
    // 람다함수 앞 (Int) -> Unit 이 매개변수가 한개임을 알려줌
    // 매개변수가 한개일때는 it 키워드로 매개변수를 이용가능
    
    some(10)
    some2(10)

    // 람다 함수는 return 문을 사용할수 없음, 반환값은 본문에서 마지막줄의 실행 결과
    val some3 = {no1:Int, no2:Int ->
        println("in lamda func")
        no1 * no2
    }
    println("result : ${some3(10, 2)}")

    // 함수 타입 선언 : 매개변수와 반환 타입을 의미
    val some4: (Int, Int) -> Int = {no1:Int, no2:Int -> no1 + no2}
    println("result : ${some4(10, 2)}")
}

class MyClass{
    companion object{
        var data = 10
        fun some(){
            println(data)
        }
    }
}

fun classTest3(){
    data class DataClass(val name:String, val email:String, val age:Int){
        lateinit var address:String
        constructor(name:String, email:String, age:Int, address:String) : this(name, email, age){
            this.address = address
        }
    }

    val obj1 = DataClass("kk", "aa", 10, "seoul")
    val obj2 = DataClass("kk", "aa", 10, "va")
    println(obj1.equals(obj2))

    MyClass.some()
    MyClass.data = 20
    MyClass.some()
}

fun classTest2(){
    open class Super{ // 상속할 수 있게 open 키워드 사용
        open var someData = 10
        open fun someFun(){
            println("i am super $someData")
        }

        public var a:Int = 10
        internal var b:Int = 10
        protected var c:Int = 10
        private var d:Int = 10
    }

    class Sub: Super(){ // 선언부에 콜론(:)과 함께 상속받을 클래스 이름을 입력
        override var someData = 20
        override fun someFun() {
            println("i am sub $someData $a $b $c")
        }
    }

    val obj = Sub()
    obj.someFun()
}

fun classTest1(){
    class User2(val name:String, val count:Int){
        init {
            println("HI init $name $count")
        }

        fun someFun(){
            println("name : $name / count $count")
        }

        inner class someClass{}
    }

    class User(name:String){
        lateinit var name:String
        var count:Int = 0
        lateinit var email:String

        init {
            this.name = name
        }

        constructor(name:String, count:Int): this(name){
            this.name = name
            this.count = count
        }

        constructor(name:String, count:Int, email:String): this(name, count){
            this.name = name
            this.count = count
            this.email = email
        }

        fun someFun(){
            println("name : $name / count $count")
        }

        inner class someClass{}
    }

    class Prop2(name: String){
        var name: String = name
            get() = field.uppercase()
            set(value) {
                if(value[0] > '5') field = value
                else field = "fkdaf"
            }
    }

    val user1:User = User("kim")
    val user2:User = User("kim", 20)
    val user3:User = User("kim", 20, "ggkdk")

    user1.someFun()
    user2.someFun()
    user3.someFun()
}

fun loopTest(){
    for(i in 1..10) println(i)
    for(i in 1 until 10) println(i)
    for(i in 1 until 10 step 2) println(i)
    for(i in 10 downTo 1) println(i)

    var data = arrayOf<Int>(10, 20, 30)
    for(i in data.indices) println(i)
    for((i, v) in data.withIndex()) println("$i / $v")
}

fun varTest(){
    val data4:Int by lazy {
        println("in lazy")
        10
    }

    println(data4)
    println(data4)

    var list = listOf<Int>(10, 20, 30)

    println("""
        list size = ${list.size}
        list data = ${list}
    """.trimIndent())
}