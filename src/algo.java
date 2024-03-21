import java.util.Scanner;

public class algo {
    // Function to check if a page hit occurs
    static boolean checkHit(int incomingPage, int[] queue, int occupied) {
        for (int i = 0; i < occupied; i++) {
            if (incomingPage == queue[i])
                return true;
        }
        return false;
    }

    // Function to print the current frame
    static void printFrame(int[] queue, int occupied) {
        for (int i = 0; i < queue.length; i++)
            System.out.print((i < occupied ? queue[i] : "") + "\t\t");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the incoming stream (space-separated integers): ");
        String incomingStreamInput = scanner.nextLine();
        String[] incomingStreamStringArray = incomingStreamInput.split(" ");
        int n = incomingStreamStringArray.length;
        int[] incomingStream = new int[n];
        for (int i = 0; i < n; i++) {
            incomingStream[i] = Integer.parseInt(incomingStreamStringArray[i]);
        }

        System.out.print("Enter the number of frames: ");
        int frames = scanner.nextInt();

        int[] queue = new int[frames];
        int[] distance = new int[frames];
        int occupied = 0;
        int pagefault = 0;
        int pagehits = 0;

        System.out.print("\n\nPAGE\t");
        for (int i = 1; i <= frames; i++)
            System.out.print(" F" + (i-1) + "\t\t");
        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.print(incomingStream[i] + ": \t\t");

            // Check if page hit occurs
            if (checkHit(incomingStream[i], queue, occupied)) {
                // Page hit, increment page hit counter and print the current frame
                pagehits++;
                printFrame(queue, occupied);
                System.out.print("\t\tHIT " + pagehits);
            } else if (occupied < frames) {
                // Page fault, and there is still space in the frame
                queue[occupied] = incomingStream[i];
                pagefault++;
                occupied++;
                printFrame(queue, occupied);
                System.out.print("\t\tFAULT " + pagefault);
            } else {
                // Page fault, and all frames are occupied
                int max = Integer.MIN_VALUE;
                int index = -1;

                // Calculate the distance of each page in the frame from the current position
                for (int j = 0; j < frames; j++) {
                    distance[j] = 0;
                    for (int k = i - 1; k >= 0; k--) {
                        ++distance[j];
                        if (queue[j] == incomingStream[k])
                            break;
                    }
                    // Find the page with the maximum distance
                    if (distance[j] > max) {
                        max = distance[j];
                        index = j;
                    }
                }
                // Replace the page with the maximum distance
                queue[index] = incomingStream[i];
                printFrame(queue, occupied);
                pagefault++;
                System.out.print("\t\tFAULT " + pagefault);
            }
            System.out.println();
        }

        // Calculate page hit rate and page fault rate
        double pageHitRate = (double) pagehits / n * 100;
        double pageFaultRate = (double) pagefault / n * 100;

        System.out.println("\nPAGE HITS: " + pagehits);
        System.out.println("HIT RATE: " + pageHitRate + "%");
        System.out.println("\nPAGE FAULTS: " + pagefault);
        System.out.println("FAULT RATE: " + pageFaultRate + "%");
    }
}
