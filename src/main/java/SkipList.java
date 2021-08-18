import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

/**
 * @author guanwu
 * @created on 2021-08-18 09:48:19
 **/

public class SkipList <T>{

    static class SkipNode<T> {
        int key;
        T value;
        SkipNode<T> right, down;
        public SkipNode(int key, T value) {
            this.key = key;
            this.value = value;
        }
    }

    SkipNode<T> headNode;
    int highLevel;
    Random random;
    public  final static int MAX_LEVEL = 32;
    SkipList() {
        random = new Random();
        headNode = new SkipNode<>(Integer.MIN_VALUE,
                null);
        highLevel = 0;
    }

    public SkipNode<T> search(int key) {
        SkipNode<T> team = headNode;
        while(Objects.nonNull(team)) {
            if (team.key == key) {
                return team;
            } else if (team.right == null) {
                team = team.down;
            } else if (team.right.key > key) {
                team = team.down;
            } else {
                team = team.right;
            }
        }
        return null;
    }

    public void delete(int key) {
        SkipNode<T> team = headNode;
        while(Objects.nonNull(team)) {
            if (team.right == null) {
                team = team.down;
            } else if (team.right.key == key) {
                team.right = team.right.right;//删除节点
                team = team.down;//向下继续删除
            } else if (team.right.key > key) {
                team = team.down;
            } else {
                team = team.right;
            }
        }
    }

    public void add(SkipNode<T> node) {
        int key = node.key;
        SkipNode<T> findNode =  search(key);
        if (Objects.nonNull(findNode)) {
            findNode.value = node.value;
        }
        Deque<SkipNode<T>> stack = new LinkedList<>();
        SkipNode<T> team = headNode;
        while (Objects.nonNull(team)) {
            if (Objects.isNull(team.right) || team.right.key > key) {
                stack.push(team);
                team = team.down;
            } else {
                team = team.right;
            }
        }
        int level = 1;
        SkipNode<T> downNode = null;
        while(!stack.isEmpty()) {
            team = stack.pop();
            SkipNode<T> newNode = new SkipNode<>(node.key, node.value);
            newNode.down = downNode;
            downNode = newNode;
            if (Objects.nonNull(team.right)) {
                //右侧不为空，需要把新节点的右侧接上
                newNode.right = team.right;
            }
            //把前驱节点的right指向新节点
            team.right = newNode;
            if (level > MAX_LEVEL) {
                break;
            }
            double randomNum = random.nextDouble();
            if (randomNum > 0.5) {
                break;
            }
            level++;
            if (level > highLevel) {//比当前高度大，而且在允许范围内
                highLevel = level;
                SkipNode<T> newHead = new SkipNode<>(Integer.MIN_VALUE, null);
                newHead.down = headNode;
                headNode = newHead;
                stack.push(headNode);
            }
        }
    }

    public void printList() {
        SkipNode<T> teamNode = headNode;
        int index = 1;
        SkipNode<T> last = teamNode;
        while (last.down != null) {
            last = last.down;
        }
        while (teamNode != null) {
            SkipNode<T> enumNode = teamNode.right;
            SkipNode<T> enumLast = last.right;
            System.out.printf("%-8s", "head->");
            while (enumLast != null && enumNode != null) {
                if (enumLast.key == enumNode.key) {
                    System.out.printf("%-5s", enumLast.key + "->");
                    enumLast = enumLast.right;
                    enumNode = enumNode.right;
                } else {
                    enumLast = enumLast.right;
                    System.out.printf("%-5s", "");
                }

            }
            teamNode = teamNode.down;
            index++;
            System.out.println();
        }
    }

    public static void main(String[] args) {
        SkipList<Integer>list=new SkipList<Integer>();
        for(int i=1;i<20;i++)
        {
            list.add(new SkipNode<>(i, 666));
        }
        list.printList();
        list.delete(4);
        list.delete(8);
        list.printList();
        SkipNode<Integer> findNode = list.search(6);
        System.out.println(findNode.key + " : " + findNode.value);
    }
}
