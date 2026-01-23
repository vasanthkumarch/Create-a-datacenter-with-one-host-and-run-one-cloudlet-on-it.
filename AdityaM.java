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
