package AgentManagement.com.example.AgentManagement;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        System.out.println("Agent Management System");

        Configuration configuration = new Configuration().configure().addAnnotatedClass(Agent.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Create Agent");
                System.out.println("2. Get Agent by ID");
                System.out.println("3. Update Agent");
                System.out.println("4. Delete Agent");
                System.out.println("5. Show All Agents");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        createAgent(session);
                        break;
                    case 2:
                        getAgentById(session);
                        break;
                    case 3:
                        updateAgent(session);
                        break;
                    case 4:
                        deleteAgent(session);
                        break;
                    case 5:
                        showAllAgents(session);
                        break;
                    case 6:
                        sessionFactory.close();
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
        }
    }

    private static void createAgent(Session session) {
        Transaction transaction = session.beginTransaction();
        Agent agent = new Agent();
        System.out.print("Enter Agent Name: ");
        agent.setName(new Scanner(System.in).nextLine());
        System.out.print("Enter Agent Email: ");
        agent.setEmail(new Scanner(System.in).nextLine());
        System.out.print("Enter Agent Phone Number: ");
        agent.setPhoneNumber(new Scanner(System.in).nextLine());
        session.save(agent);
        transaction.commit();
        System.out.println("Agent created successfully!");
    }

    private static void getAgentById(Session session) {
        System.out.print("Enter Agent ID: ");
        int agentId = new Scanner(System.in).nextInt();
        Agent agent = session.get(Agent.class, agentId);
        if (agent != null) {
            System.out.println("Agent Details:");
            System.out.println("Agent ID: " + agent.getAgentId());
            System.out.println("Name: " + agent.getName());
            System.out.println("Email: " + agent.getEmail());
            System.out.println("Phone Number: " + agent.getPhoneNumber());
        } else {
            System.out.println("Agent not found.");
        }
    }

    private static void updateAgent(Session session) {
        System.out.print("Enter Agent ID to update: ");
        int agentId = new Scanner(System.in).nextInt();
        Agent agent = session.get(Agent.class, agentId);
        if (agent != null) {
            Transaction transaction = session.beginTransaction();
            System.out.print("Enter new Phone Number: ");
            agent.setPhoneNumber(new Scanner(System.in).nextLine());
            session.update(agent);
            transaction.commit();
            System.out.println("Agent updated successfully!");
        } else {
            System.out.println("Agent not found.");
        }
    }

    private static void deleteAgent(Session session) {
        System.out.print("Enter Agent ID to delete: ");
        int agentId = new Scanner(System.in).nextInt();
        Agent agent = session.get(Agent.class, agentId);
        if (agent != null) {
            Transaction transaction = session.beginTransaction();
            session.delete(agent);
            transaction.commit();
            System.out.println("Agent deleted successfully!");
        } else {
            System.out.println("Agent not found.");
        }
    }

    private static void showAllAgents(Session session) {
        System.out.println("All Agents:");
        // Fetch all agents from the database
        session.createQuery("FROM Agent", Agent.class)
                .getResultStream()
                .forEach(agent -> {
                    System.out.println("Agent ID: " + agent.getAgentId());
                    System.out.println("Name: " + agent.getName());
                    System.out.println("Email: " + agent.getEmail());
                    System.out.println("Phone Number: " + agent.getPhoneNumber());
                    System.out.println("-----------------------");
                });
    }
}
