package no.ntnu;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Process {
    int processId;
    int arrivalTime;
    int burstTime;
    int priority;

    Process(int processId, int arrivalTime, int burstTime, int priority) {
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }
}

public class CpuScheduling {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Getting user input for FCFS scheduling
        List<Process> fcfsProcesses = getUserInputFCFS(scanner);
        // Getting user input for Preemptive Priority scheduling
        List<Process> priorityProcesses = getUserInputPreemptivePriority(scanner);

        scanner.close();

        // Applying FCFS scheduling algorithm
        fcfs(fcfsProcesses);
        // Applying Preemptive Priority scheduling algorithm
        preemptivePriority(priorityProcesses);
    }

    // FCFS Scheduling Algorithm
    static void fcfs(List<Process> processes) {
        // Sort the processes based on arrival time
        Collections.sort(processes, (p1, p2) -> p1.arrivalTime - p2.arrivalTime);

        int n = processes.size();
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];

        waitingTime[0] = 0;
        turnaroundTime[0] = processes.get(0).burstTime;

        // Calculating waiting and turnaround time for each process
        for (int i = 1; i < n; i++) {
            waitingTime[i] = turnaroundTime[i - 1];
            turnaroundTime[i] = waitingTime[i] + processes.get(i).burstTime;
        }

        // Calculating average waiting time and average turnaround time
        double avgWaitingTime = calculateAverage(waitingTime);
        double avgTurnaroundTime = calculateAverage(turnaroundTime);

        // Displaying results
        System.out.println("\nFCFS Scheduling:");
        System.out.println("Average Waiting time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    }

    // Preemptive Priority Scheduling Algorithm
    static void preemptivePriority(List<Process> processes) {
        // Sort the processes based on arrival time and priority
        Collections.sort(processes, (p1, p2) -> {
            if (p1.arrivalTime != p2.arrivalTime) {
                return p1.arrivalTime - p2.arrivalTime;
            }
            return p1.priority - p2.priority;
        });

        int n = processes.size();
        int[] remainingBurstTime = processes.stream().mapToInt(p -> p.burstTime).toArray();
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];
        int completed = 0;
        int currentTime = 0;

        // Execute until all processes are completed
        while (completed < n) {
            int selectedProcess = -1;
            int highestPriority = Integer.MAX_VALUE;

            // Find the process with highest priority
            for (int i = 0; i < n; i++) {
                if (processes.get(i).arrivalTime <= currentTime && remainingBurstTime[i] > 0 &&  (processes.get(i).priority < highestPriority)) {
                        highestPriority = processes.get(i).priority;
                        selectedProcess = i;
                    
                }
            }

            if (selectedProcess == -1) {
                currentTime++;
                continue;
            }

            // Execute the selected process for one unit
            remainingBurstTime[selectedProcess]--;

            // If process is completed
            if (remainingBurstTime[selectedProcess] == 0) {
                completed++;
                turnaroundTime[selectedProcess] = currentTime + 1 - processes.get(selectedProcess).arrivalTime;
                waitingTime[selectedProcess] = turnaroundTime[selectedProcess] - processes.get(selectedProcess).burstTime;
            }

            currentTime++;
        }

        // Calculating average waiting time and average turnaround time
        double avgWaitingTime = calculateAverage(waitingTime);
        double avgTurnaroundTime = calculateAverage(turnaroundTime);

        // Displaying results
        System.out.println("\nPreemptive Priority Scheduling:");
        System.out.println("Average Waiting time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    }

    // Method to get user input for FCFS scheduling
    static List<Process> getUserInputFCFS(Scanner scanner) {
        List<Process> processes = new ArrayList<>();
        System.out.println("Enter number of processes for FCFS:");
        int n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.println("Enter details for process " + (i + 1) + ":");
            System.out.print("Process ID: ");
            int processId = scanner.nextInt();
            System.out.print("Arrival Time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Burst Time: ");
            int burstTime = scanner.nextInt();

            processes.add(new Process(processId, arrivalTime, burstTime, 0));
        }
        return processes;
    }

    // Method to get user input for Preemptive Priority scheduling
    static List<Process> getUserInputPreemptivePriority(Scanner scanner) {
        List<Process> processes = new ArrayList<>();
        System.out.println("\nEnter number of processes for Preemptive Priority:");
        int n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.println("Enter details for process " + (i + 1) + ":");
            System.out.print("Process ID: ");
            int processId = scanner.nextInt();
            System.out.print("Arrival Time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Burst Time: ");
            int burstTime = scanner.nextInt();
            System.out.print("Priority: ");
            int priority = scanner.nextInt();

            processes.add(new Process(processId, arrivalTime, burstTime, priority));
        }
        return processes;
    }

    // Method to calculate average of an array of integers
    static double calculateAverage(int[] array) {
        return array.length > 0 ? (double) sum(array) / array.length : 0;
    }

    // Method to calculate sum of an array of integers
    static int sum(int[] array) {
        int sum = 0;
        for (int num : array) {
            sum += num;
        }
        return sum;
    }
}
