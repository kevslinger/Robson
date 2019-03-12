public class Stack {
    Node head;

    public Stack(){
        head = new Node();
    }
    class Node {
        //Each node has an integer value and a link to the next Node.
        BTNode val;
        Node next;

        //Constructor when only an integer is supplied.
        Node() {
            next = null;
        }

        //Constructor when both an integer and a next node is supplied.
        Node(BTNode val, Node next) {
            this.val = val;
            this.next = next;
        }
    }
    //Given an integer, insert a Node with that integer value to the list.
    void push(BTNode val){//Adding a node takes constant time.
        //Create a new node with head.next as the next connection.
        //Then make head.next be the new node.
        Node ret = new Node(val, head.next);
        head.next = ret;
    }
    void push(Node n){
        //Pushes node onto stack.
        head.next = n;
    }
    //Given an integer and a Node, insert a Node with that integer value after the Node given.
    void push(BTNode val, Node n){//Again, adding takes constant time.
        Node ret = new Node();
        ret.val = val;
        n.next = ret;
    }
    //Removes the first item in the list
    BTNode pop(){//Removing takes constant time.
        Node ret = head.next;
        head.next = ret.next;
        return ret.val;
    }
    BTNode peek(){
        if (head.next == null){
            return null;
        }
        return head.next.val;
    }
    //Function to return length of the LL
    int length(){//Finding the length takes worst-case O(n) time.
        int count = 0;
        Node tmp = head;
        while(tmp.next != null){
            count++;
            tmp = tmp.next;
        }
        return count;
    }
    @Override
    public String toString(){
        Node tmp = head;
        String ret = "";
        while (tmp.next != null){
            ret += tmp.next.val + " ";
            tmp = tmp.next;
        }
        return ret;
    }
}
