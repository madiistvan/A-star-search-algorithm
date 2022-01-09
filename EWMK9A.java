import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static int search_num;
    public static int nodes_num;
    public static int lines_num;
    public static ArrayList<Direction> directions = new ArrayList<Direction>();
    public static ArrayList<Node> nodes = new ArrayList<Node>();
    public static ArrayList<Line> lines = new ArrayList<Line>();

    public static double length(int x1,int y1,int x2,int y2){
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }
    public static void main(String[] args) {
        readInput();
        for(int i = 0;i<search_num;i++){
            System.out.print(String.format("%.2f",pathFinder(directions.get(i))));
            if(search_num-1 != i) System.out.print("\t");
        }
    }

    public static  void print(String s){
        System.out.println(s);
    }
    public static void readInput(){
        int start, end = 0;
        search_num = scanner.nextInt();
        nodes_num = scanner.nextInt();
        lines_num = scanner.nextInt();
        for(int i = 0;i<search_num;i++){
            directions.add(new Direction(scanner.nextInt(),scanner.nextInt()));
        }
        for(int i = 0;i<nodes_num;i++){
            nodes.add(new Node(scanner.nextInt(),scanner.nextInt(),i));
        }
        for(int i = 0;i<lines_num;i++){
            start = scanner.nextInt();
            end = scanner.nextInt();
            lines.add(new Line(start,end));
            nodes.get(start).neighbours.add(nodes.get(end));
            nodes.get(end).neighbours.add(nodes.get(start));
        }
    }
    public static void writeInput(){
        print("Pontparok szama melyek között utat kell keresni: "+search_num);
        print("Az uthalozat csucsainak szama " + nodes_num);
        print("Az uthalozat utszakaszainak szama: "+lines_num);
        for(int i = 0;i<search_num;i++){
            print("Kezdopont"+directions.get(i).startPos+"es vegpont: "+directions.get(i).endPos+" kozott utat keresunk");
        }
        for(int i = 0;i<nodes_num;i++){
            print("Az "+i+"-edik csomopont X kordinataja: " +nodes.get(i).X + " Y kordinataja: "+nodes.get(i).Y);
            for (Node node : nodes.get(i).neighbours) {
                print(node.toString());
            }
        }
        for(int i = 0;i<lines_num;i++){
            print(lines.get(i).startNode+" es "+ lines.get(i).endNode+ " szamu csomopontok kozott "+lines.get(i).length+ " hosszu ut megy");
        }
    }
    public static Node minCost(ArrayList<Node> nodes_){
        Node res = nodes_.get(0);
        for (Node node : nodes_) {
            if(res.f()>node.f()) res = node;
            if(res.f()==node.f()){
                if(res.h>node.h) res = node;
            }
        }
        return res;
    }
    public static double pathLength(ArrayList<Line> path){
        double total_length = 0.0;
        for (Line line : path) {
            total_length += line.length;
        }
        return total_length;
    }
    public static ArrayList<Line> getPath(Node start, Node end){
        Node current = end;
        ArrayList<Line> path = new ArrayList<Line>();

        while(current != start){
            path.add(new Line(current,current.parent));
            current= current.parent;
        }
        return path;
    }
    public static double pathFinder(Direction dir){
        Node start = nodes.get(dir.startPos);
        Node end = nodes.get(dir.endPos);
        Node current;
        ArrayList<Node> tobe_evaled_nodes = new ArrayList<Node>();
        ArrayList<Node> evaled_nodes = new ArrayList<Node>();

        tobe_evaled_nodes.add(start);


        while(tobe_evaled_nodes.size()!=0){
            current=minCost(tobe_evaled_nodes);

            tobe_evaled_nodes.remove(current);
            evaled_nodes.add(current);
            
            if(current==end) return pathLength(getPath(start, end));
            for(Node node : current.neighbours) {
                if(evaled_nodes.contains(node)) continue;

                double neighbour_dist = current.g + current.dist(node);

                if(node.g > neighbour_dist || !tobe_evaled_nodes.contains(node)){
                    node.g = neighbour_dist;
                    node.h = node.dist(end);
                    node.parent = current;
                    if(!tobe_evaled_nodes.contains(node))tobe_evaled_nodes.add(node);
                }
            }
        }
        return 0.0;
    }
}

class Node{
    int ID;
    int X;
    int Y;

    double h=0;
    double g=0;

    public ArrayList<Node> neighbours = new ArrayList<Node>();

    Node parent;

    Node(int x, int y, int id){
        X=x;
        Y=y;
        ID=id;
    }
    public double dist(Node end){
        return Main.length(end.X,end.Y,X,Y);
    }
    public String toString(){
        return ID+" ("+X+","+Y+")";
    }
    public double f(){
        return g+h;
    }
}
class Direction{
    public int startPos;
    public int endPos;
    Direction(int start, int end){
        startPos=start;
        endPos=end;
    }
}
class Line {
    public Node startNode;
    public Node endNode;
    double length;
    Line(int start,int end){
        startNode = Main.nodes.get(start);
        endNode = Main.nodes.get(end);
        length = Main.length(startNode.X, startNode.Y, endNode.X, endNode.Y);
    }
    Line(Node start, Node end){
        startNode=start;
        endNode=end;
        length = Main.length(startNode.X, startNode.Y, endNode.X, endNode.Y);
    }
}