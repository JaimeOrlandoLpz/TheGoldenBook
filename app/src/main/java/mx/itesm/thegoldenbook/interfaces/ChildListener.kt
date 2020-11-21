package mx.itesm.thegoldenbook.interfaces

interface ChildListener<T> {
    fun onChildAdded(position: Int, model: T)
    fun onChildChanged(position: Int, model: T)
    fun onChildRemoved(position: Int, model: T)
    fun onChildMoved(model: T)
}