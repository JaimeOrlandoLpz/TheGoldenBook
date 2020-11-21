package mx.itesm.thegoldenbook.interfaces

interface ChildListener<T> {
    fun onChildAdded(model: T)
    fun onChildChanged(model: T)
    fun onChildRemoved(id: String)
    fun onChildMoved(model: T)
}