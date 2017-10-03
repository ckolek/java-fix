package me.kolek.fix.engine.quickfixj;

import org.apache.commons.cli.*;

import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Launcher {
    private static final Options options;
    private static final String OPT_REGISTRY_HOST = "rh";
    private static final String OPT_REGISTRY_PORT = "rp";
    private static final String OPT_BIND_NAME = "bn";
    private static final String OPT_BIND_PORT = "bp";
    private static final String OPT_STORE_DIR = "sd";
    private static final String OPT_LOG_DIR = "ld";
    private static final String OPT_HELP = "h";

    static {
        options = new Options();
        options.addOption(OPT_REGISTRY_HOST, "registryHost", true, "RMI Registry hostname");
        options.addOption(OPT_REGISTRY_PORT, "registryPort", true, "RMI Registry port");
        options.addOption(OPT_BIND_NAME, "bindName", true, "Name to bind FIX engine factory to");
        options.addOption(OPT_BIND_PORT, "bindPort", true, "Port to export FIX engine factory to");
        options.addOption(OPT_STORE_DIR, "storeDir", true, "Directory to store message data");
        options.addOption(OPT_LOG_DIR, "logDir", true, "Directory to store logs");
        options.addOption(OPT_HELP, "help", false, "Display usage information");
    }

    public static void main(String[] args) throws Exception {
        CommandLineParser parser = new BasicParser();

        CommandLine cl;
        try {
            cl = parser.parse(options, args);
        } catch (ParseException e) {
            usage(e.getMessage());
            return;
        }

        if (cl.hasOption(OPT_HELP)) {
            usage(null);
            return;
        }
        String registryHost = getOptionValue(cl, OPT_REGISTRY_HOST, null);
        int registryPort = getOptionIntValue(cl, OPT_REGISTRY_PORT, Registry.REGISTRY_PORT);
        if (registryPort < 0) {
            return;
        }
        String bindName = getOptionValue(cl, OPT_BIND_NAME, "EngineFactory");
        int bindPort = getOptionIntValue(cl, OPT_BIND_PORT, 0);
        if (bindPort < 0) {
            return;
        }

        String messageStoreDir = getOptionValue(cl, OPT_STORE_DIR, "data");
        String logDir = getOptionValue(cl, OPT_LOG_DIR, "logs");

        QfjFixEngineFactory factory = launch(registryHost, registryPort, bindName, bindPort);
        factory.setMessageStoreDir(new File(messageStoreDir));
        factory.setLogDir(new File(logDir));
    }

    private static String getOptionValue(CommandLine cl, String opt, String defaultValue) {
        String value = cl.getOptionValue(opt);
        return (value != null) ? value : defaultValue;
    }

    private static int getOptionIntValue(CommandLine cl, String opt, int defaultValue) {
        String value = cl.getOptionValue(opt);
        try {
            return (value != null) ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            usage(options.getOption(opt).getArgName() + " must be a valid integer");
            return -1;
        }
    }

    private static QfjFixEngineFactory launch(String registryHost, int registryPort, String bindName, int bindPort)
            throws Exception {
        QfjFixEngineFactory factory = new QfjFixEngineFactory(bindPort);

        Registry registry;
        if (registryHost != null) {
            registry = LocateRegistry.getRegistry(registryHost, registryPort);
        } else {
            registry = LocateRegistry.createRegistry(registryPort);
        }

        registry.bind(bindName, factory);

        System.out.printf("FIX engine factory available at rmi://%s:%d/%s\n",
                (registryHost != null) ? registryHost : "localhost", registryPort, bindName);

        return factory;
    }

    private static void usage(String error) {
        if (error != null) {
            System.err.println(error);
        }
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Launcher", options);
    }
}
