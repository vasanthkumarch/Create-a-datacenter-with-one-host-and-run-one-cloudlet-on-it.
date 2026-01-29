## Create-a-datacenter-with-one-host-and-run-one-cloudlet-on-it.


## Experiment 


# Create a Datacenter with One Host and Run One Cloudlet (CloudSim)

This repository demonstrates a **basic CloudSim simulation** where a single datacenter with one host is created, and one cloudlet is executed on it. The project is intended for **cloud computing laboratory experiments** and beginner-level understanding of CloudSim architecture.

---

## Aim

To create a simple cloud computing environment using CloudSim by configuring one datacenter with a single host and executing one cloudlet, and to observe the simulation results.



## Objectives

* Understand the working of the CloudSim toolkit
* Learn how to create a datacenter and host in CloudSim
* Execute a single cloudlet on a virtual machine
* Analyze cloudlet execution output

---

##  Software Requirements

* Operating System: Windows / Linux / macOS
* Java Development Kit (JDK): 8 or above
* IDE: Eclipse / IntelliJ IDEA / NetBeans
* CloudSim Toolkit: Version 3.x

---

##  Hardware Requirements

* Processor: Intel i3 or higher
* RAM: Minimum 4 GB
* Storage: At least 10 GB free disk space

---

## Project Structure

```
Create-a-datacenter-with-one-host-and-run-one-cloudlet-on-it/
│
├── src/
│   └── DatacenterSingleHostSingleCloudlet.java
├── lib/
│   └── cloudsim-3.x.jar
├── output/
│   └── simulation_output.png
├── README.md
```

---



##  Steps to Run the Project

1. Install Java JDK (8 or above).
2. Download and configure the CloudSim library.
3. Import the project into your IDE.
4. Add CloudSim JAR files to the project build path.
5. Compile and run the Java program.
6. Observe the cloudlet execution results in the console.

---

##  Simulation Workflow

1. Initialize CloudSim
2. Create a Datacenter with one Host
3. Create a Broker
4. Create one Virtual Machine (VM)
5. Create one Cloudlet
6. Bind the Cloudlet to the VM
7. Start Simulation
8. Display Results

---
## Program
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;

import java.util.*;

public class OneDatacenterOneCloudlet {

    public static void main(String[] args) {

        try {
            // Step 1: Initialize CloudSim
            int numUsers = 1;
            Calendar calendar = Calendar.getInstance();
            boolean traceFlag = false;
            CloudSim.init(numUsers, calendar, traceFlag);

            // Step 2: Create Datacenter
            Datacenter datacenter = createDatacenter("Datacenter_1");

            // Step 3: Create Broker
            DatacenterBroker broker = new DatacenterBroker("Broker_1");
            int brokerId = broker.getId();

            // Step 4: Create VM
            List<Vm> vmList = new ArrayList<>();
            Vm vm = new Vm(
                    0, brokerId, 1000, 1,
                    512, 1000, 10000,
                    "Xen", new CloudletSchedulerTimeShared()
            );
            vmList.add(vm);
            broker.submitVmList(vmList);

            // Step 5: Create Cloudlet
            List<Cloudlet> cloudletList = new ArrayList<>();
            Cloudlet cloudlet = new Cloudlet(
                    0, 40000, 1,
                    300, 300,
                    new UtilizationModelFull(),
                    new UtilizationModelFull(),
                    new UtilizationModelFull()
            );
            cloudlet.setUserId(brokerId);
            cloudletList.add(cloudlet);
            broker.submitCloudletList(cloudletList);

            // Step 6: Start Simulation
            CloudSim.startSimulation();

            // Step 7: Stop Simulation
            CloudSim.stopSimulation();

            // Step 8: Print Results
            List<Cloudlet> resultList = broker.getCloudletReceivedList();
            printCloudletResults(resultList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Datacenter createDatacenter(String name) {

        List<Host> hostList = new ArrayList<>();

        List<Pe> peList = new ArrayList<>();
        peList.add(new Pe(0, new PeProvisionerSimple(1000)));

        Host host = new Host(
                0,
                new RamProvisionerSimple(2048),
                new BwProvisionerSimple(10000),
                1000000,
                peList,
                new VmSchedulerTimeShared(peList)
        );

        hostList.add(host);

        DatacenterCharacteristics characteristics =
                new DatacenterCharacteristics(
                        "x86", "Linux", "Xen",
                        hostList, 10.0, 3.0,
                        0.05, 0.001, 0.0
                );

        try {
            return new Datacenter(
                    name,
                    characteristics,
                    new VmAllocationPolicySimple(hostList),
                    new LinkedList<>(),
                    0
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void printCloudletResults(List<Cloudlet> list) {

        System.out.println("\n========== CLOUDLET EXECUTION RESULTS ==========");
        System.out.println("Cloudlet ID | STATUS | Datacenter ID | VM ID | Time | Start | Finish");

        for (Cloudlet cloudlet : list) {
            System.out.println(
                    cloudlet.getCloudletId() + "\t\t" +
                    (cloudlet.getStatus() == Cloudlet.SUCCESS ? "SUCCESS" : "FAILED") + "\t\t" +
                    cloudlet.getResourceId() + "\t\t" +
                    cloudlet.getVmId() + "\t" +
                    cloudlet.getActualCPUTime() + "\t" +
                    cloudlet.getExecStartTime() + "\t" +
                    cloudlet.getFinishTime()
            );
        }
    }
}









##  Output
<img width="1109" height="982" alt="image" src="https://github.com/user-attachments/assets/df2c4837-d7ac-4e80-a9d7-8467e3ac4f7f" />



The simulation displays:

* Cloudlet ID
* Execution status
* Datacenter ID
* VM ID
* Start time
* Finish time

 

##  Output Screenshot

Add the console output screenshot in the `output/` folder and name it as `simulation_output.png`.

---

##  Applications

* Cloud Computing Laboratory Experiments
* Learning CloudSim Basics
* Academic Demonstrations

---

##  Conclusion

This project successfully demonstrates the creation of a simple cloud environment using CloudSim, where a single cloudlet is executed on one host. It provides a strong foundation for understanding more complex cloud simulations.

---

 

---

##  License

This project is intended for educational and academic use only.

