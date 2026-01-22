import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.*;

import java.text.DecimalFormat;
import java.util.*;

public class DatacenterSingleHostSingleCloudlet {

    private static List<Cloudlet> cloudletList;
    private static List<Vm> vmlist;

    public static void main(String[] args) {

        Log.printLine("Starting CloudSim Simulation...");

        try {
            // Step 1: Initialize CloudSim
            int numUsers = 1;
            Calendar calendar = Calendar.getInstance();
            boolean traceFlag = false;

            CloudSim.init(numUsers, calendar, traceFlag);

            // Step 2: Create Datacenter
            Datacenter datacenter0 = createDatacenter("Datacenter_0");

            // Step 3: Create Broker
            DatacenterBroker broker = new DatacenterBroker("Broker");
            int brokerId = broker.getId();

            // Step 4: Create Virtual Machine
            vmlist = new ArrayList<>();

            int vmId = 0;
            int mips = 1000;
            long size = 10000; // VM image size (MB)
            int ram = 512;     // VM memory (MB)
            long bw = 1000;
            int pesNumber = 1;
            String vmm = "Xen";

            Vm vm = new Vm(
                    vmId,
                    brokerId,
                    mips,
                    pesNumber,
                    ram,
                    bw,
                    size,
                    vmm,
                    new CloudletSchedulerTimeShared()
            );

            vmlist.add(vm);
            broker.submitVmList(vmlist);

            // Step 5: Create Cloudlet
            cloudletList = new ArrayList<>();

            int cloudletId = 0;
            long length = 400000;
            long fileSize = 300;
            long outputSize = 300;
            UtilizationModel utilizationModel = new UtilizationModelFull();

            Cloudlet cloudlet = new Cloudlet(
                    cloudletId,
                    length,
                    pesNumber,
                    fileSize,
                    outputSize,
                    utilizationModel,
                    utilizationModel,
                    utilizationModel
            );

            cloudlet.setUserId(brokerId);
            cloudlet.setVmId(vmId);

            cloudletList.add(cloudlet);
            broker.submitCloudletList(cloudletList);

            // Step 6: Start Simulation
            CloudSim.startSimulation();

            // Step 7: Stop Simulation
            CloudSim.stopSimulation();

            // Step 8: Display Results
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            printCloudletResults(newList);

            Log.printLine("CloudSim Simulation finished!");

        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("Simulation terminated due to an error");
        }
    }

    /**
     * Creates a Datacenter with one Host
     */
    private static Datacenter createDatacenter(String name) {

        List<Host> hostList = new ArrayList<>();

        // Host specifications
        int mips = 1000;
        int pesNumber = 1;
        int ram = 2048; // MB
        long storage = 1000000; // MB
        int bw = 10000;

        List<Pe> peList = new ArrayList<>();
        peList.add(new Pe(0, new PeProvisionerSimple(mips)));

        Host host = new Host(
                0,
                new RamProvisionerSimple(ram),
                new BwProvisionerSimple(bw),
                storage,
                peList,
                new VmSchedulerTimeShared(peList)
        );

        hostList.add(host);

        // Datacenter characteristics
        String arch = "x86";
        String os = "Linux";
        String vmm = "Xen";
        double timeZone = 10.0;
        double cost = 3.0;
        double costPerMem = 0.05;
        double costPerStorage = 0.001;
        double costPerBw = 0.0;

        DatacenterCharacteristics characteristics =
                new DatacenterCharacteristics(
                        arch,
                        os,
                        vmm,
                        hostList,
                        timeZone,
                        cost,
                        costPerMem,
                        costPerStorage,
                        costPerBw
                );

        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(
                    name,
                    characteristics,
                    new VmAllocationPolicySimple(hostList),
                    new ArrayList<Storage>(),
                    0
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }

    /**
     * Prints Cloudlet Results
     */
    private static void printCloudletResults(List<Cloudlet> list) {

        String indent = "    ";
        DecimalFormat dft = new DecimalFormat("###.##");

        System.out.println("\n========== OUTPUT ==========");
        System.out.println("Cloudlet ID" + indent + "STATUS" + indent +
                "Datacenter ID" + indent + "VM ID" + indent +
                "Start Time" + indent + "Finish Time");

        for (Cloudlet cloudlet : list) {
            System.out.print(indent + cloudlet.getCloudletId());

            if (cloudlet.getStatus() == Cloudlet.SUCCESS) {
                System.out.print(indent + "SUCCESS");
                System.out.print(indent + cloudlet.getResourceId());
                System.out.print(indent + cloudlet.getVmId());
                System.out.print(indent + dft.format(cloudlet.getExecStartTime()));
                System.out.println(indent + dft.format(cloudlet.getFinishTime()));
            }
        }
    }
}6
