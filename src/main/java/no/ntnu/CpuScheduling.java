package no.ntnu;

import java.util.*;

class Process {
    int processId;
    int arrivalTime;
    int burstTime;
    int priority;

    Process(int id, int arrival, int burst, int priority) {
        this.processId = id;
        this.arrivalTime = arrival;
        this.burstTime = burst;
        this.priority = priority;
    }
}

public class CpuScheduling {

    public static void fcfs(ArrayList<Process> processes) {
        Collections.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));

        int n = processes.size();
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];

        waitingTime[0] = 0;
        turnaroundTime[0] = processes.get(0).burstTime;

        for (int i = 1; i < n; i++) {
            waitingTime[i] = turnaroundTime[i - 1];
            turnaroundTime[i] = waitingTime[i] + processes.get(i).burstTime;
        }

        double avgWaitingTime = Arrays.stream(waitingTime).average().orElse(0);
        double avgTurnaroundTime = Arrays.stream(turnaroundTime).average().orElse(0);

        System.out.println("FCFS Scheduling:");
        System.out.println("Average Waiting time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    }

    public static void preemptivePriority(ArrayList<Process> processes) {
        Collections.sort(processes, Comparator.comparingInt(p -> p.arrivalTime)); 
        int n = processes.size();
        int[] remainingBurstTime = new int[n];
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];
        int completed = 0;
        int currentTime = 0;

        for (int i = 0; i < n; i++) {
            remainingBurstTime[i] = processes.get(i).burstTime;
        }

        while (completed < n) {
            int selectedProcess = -1;
            int highestPriority = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (processes.get(i).arrivalTime <= currentTime && remainingBurstTime[i] > 0) {
                    if (processes.get(i).priority < highestPriority) {
                        highestPriority = processes.get(i).priority;
                        selectedProcess = i;
                    }
                }
            }

            if (selectedProcess == -1) {
                currentTime++;
                continue;
            }

            remainingBurstTime[selectedProcess]--;

            if (remainingBurstTime[selectedProcess] == 0) {
                completed++;
                turnaroundTime[selectedProcess] = currentTime + 1 - processes.get(selectedProcess).arrivalTime;
                waitingTime[selectedProcess] = turnaroundTime[selectedProcess] - processes.get(selectedProcess).burstTime;
            }

            currentTime++;
        }

        double avgWaitingTime = Arrays.stream(waitingTime).average().orElse(0);
        double avgTurnaroundTime = Arrays.stream(turnaroundTime).average().orElse(0);

        System.out.println("\nPreemptive Priority Scheduling:");
        System.out.println("Average Waiting time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    }

    public static void main(String[] args) {
        ArrayList<Process> processesFcfs = new ArrayList<>();
        processesFcfs.add(new Process(1, 0, 5, 1));
        processesFcfs.add(new Process(2, 1, 3, 2));
        processesFcfs.add(new Process(3, 2, 6, 3));

        ArrayList<Process> processesPreemptivePriority = new ArrayList<>();
        processesPreemptivePriority.add(new Process(1, 0, 5, 2));
        processesPreemptivePriority.add(new Process(2, 1, 3, 1));
        processesPreemptivePriority.add(new Process(3, 2, 6, 3));

        fcfs(processesFcfs);
        preemptivePriority(processesPreemptivePriority);
    }
}
