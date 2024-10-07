import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class Prcs {
    int id;
    int burstTime;
    int arrivalTime;
    int waitingTime;
    int turnAroundTime;
    int completionTime;

    Prcs(int id, int burstTime, int arrivalTime) {
        this.id = id;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.waitingTime = 0;
        this.turnAroundTime = 0;
        this.completionTime = 0;
    }
}

public class SJF {
    static List<Prcs> executionOrder = new ArrayList<>();
    static void calculateTimes(Prcs[] processes) {
        int n = processes.length;
        int currentTime = 0;
        boolean[] completed = new boolean[n];
        int completedCount = 0;

        while (completedCount < n) {
            int idx = -1;
            int minBurstTime = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (!completed[i] && processes[i].arrivalTime <= currentTime && processes[i].burstTime < minBurstTime)
                {
                    minBurstTime = processes[i].burstTime;
                    idx = i;
                }
            }

            if (idx == -1) {
                currentTime++;
            } else {
                Prcs process = processes[idx];
                process.waitingTime = currentTime - process.arrivalTime;
                process.completionTime = currentTime + process.burstTime;
                process.turnAroundTime = process.completionTime - process.arrivalTime;
                currentTime = process.completionTime;
                completed[idx] = true;
                completedCount++;

                executionOrder.add(process);
            }
        }
    }

    static void displayGanttChart(Prcs[] processes) {
        System.out.println("\nGantt Chart:");
        int currentTime = 0;

        for (Prcs process : executionOrder) {
            if (process.arrivalTime > currentTime) {
                System.out.print(" Idle |");
                currentTime = process.arrivalTime;
            }
            System.out.print(" P" + process.id + " |");
            currentTime += process.burstTime;
        }
        System.out.println();

        currentTime = 0;
        System.out.print(currentTime);
        for (Prcs process : executionOrder) {
            if (process.arrivalTime > currentTime) {
                System.out.print("    " + process.arrivalTime);
                currentTime = process.arrivalTime;
            }
            currentTime += process.burstTime;
            System.out.print("    " + currentTime);
        }
        System.out.println();
    }

    static void displayResults(Prcs[] processes) {
        System.out.println("\nProcess\tArrival Time\tBurst Time\tWaiting Time\tTurnaround Time\tCompletion Time");
        for (Prcs process : processes) {
            System.out.println("P" + process.id + "\t\t" + process.arrivalTime + "\t\t\t\t" + process.burstTime + "\t\t\t\t" + process.waitingTime + "\t\t\t\t" + process.turnAroundTime + "\t\t\t\t" + process.completionTime);
        }

        double totalWaitingTime = 0, totalTurnAroundTime = 0;
        for (Prcs process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnAroundTime += process.turnAroundTime;
        }

        System.out.println("\nAverage Waiting Time: " + (totalWaitingTime / processes.length));
        System.out.println("Average Turnaround Time: " + (totalTurnAroundTime / processes.length));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();

        Prcs[] processes = new Prcs[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time for process P" + (i + 1) + ": ");
            int arrivalTime = sc.nextInt();
            System.out.print("Enter burst time for process P" + (i + 1) + ": ");
            int burstTime = sc.nextInt();
            processes[i] = new Prcs(i + 1, burstTime, arrivalTime);
        }
        Arrays.sort(processes, (p1, p2) -> p1.arrivalTime - p2.arrivalTime);
        calculateTimes(processes);
        displayGanttChart(processes);
        displayResults(processes);

        sc.close();
    }
}
