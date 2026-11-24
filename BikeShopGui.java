import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class BikeShopGui extends JFrame {

    private ArrayList<Product> inventory = new ArrayList<>();
    private ArrayList<CartItem> cart = new ArrayList<>();

    private JPanel centerPanel; 
    private JPanel cartItemsPanel;
    private JLabel totalAmountLabel;

    private JTextField nameInput, priceInput, ccInput, imageInput;

    private final Color BG_MAIN = new Color(30, 30, 30);
    private final Color BG_PANEL = new Color(50, 50, 50);
    private final Color ACCENT = new Color(0, 255, 200);
    private final Color TEXT_WHITE = new Color(255, 255, 255);
    private final Color BTN_RED = new Color(255, 80, 80);

    public BikeShopGui() {
        setTitle("Asphalt Atelier - Management System");
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_MAIN);

        initializeInventory();

        add(createHeader(), BorderLayout.NORTH);
        add(createMainGrid(), BorderLayout.CENTER);
        add(createCartSidebar(), BorderLayout.EAST);
        add(createAdminPanel(), BorderLayout.SOUTH);
    }

    private void initializeInventory() {
        inventory.add(new Product("CFMOTO 300 NK", 342000.0, "./Images/bike1.jpg", "298.8 cc"));
        inventory.add(new Product("Honda CBR 150R", 530000.0, "./Images/bike2.jpg", "149.2 cc"));
        inventory.add(new Product("Hero Thriller", 235000.0, "./Images/bike3.jpg", "159.6 cc"));
        inventory.add(new Product("Honda CB150X", 389000.0, "./Images/bike4.jpg", "151.3 cc"));
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 20));
        headerPanel.setBackground(BG_MAIN);

        JLabel titleLbl = new JLabel("Asphalt Atelier");
        titleLbl.setFont(new Font("Arial", Font.BOLD, 28));
        titleLbl.setForeground(ACCENT);
        
        JButton exitBtn = createButton("EXIT", BTN_RED, TEXT_WHITE);
        exitBtn.setPreferredSize(new Dimension(80, 30));
        exitBtn.addActionListener(e -> System.exit(0));

        headerPanel.add(titleLbl);
        headerPanel.add(Box.createHorizontalStrut(800));
        headerPanel.add(exitBtn);
        return headerPanel;
    }

    private JScrollPane createMainGrid() {
        centerPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        centerPanel.setBackground(BG_MAIN);
        centerPanel.setBorder(new EmptyBorder(10, 40, 40, 40));

        refreshProductGrid();

        JScrollPane scroll = new JScrollPane(centerPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private void refreshProductGrid() {
        centerPanel.removeAll();
        for (Product p : inventory) {
            centerPanel.add(createProductCard(p));
        }
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private JPanel createProductCard(Product p) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_PANEL);
        card.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        card.setPreferredSize(new Dimension(220, 320));

        JLabel img = new JLabel();
        img.setAlignmentX(CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon(p.imagePath);
            Image i = icon.getImage().getScaledInstance(200, 140, Image.SCALE_SMOOTH);
            img.setIcon(new ImageIcon(i));
        } catch (Exception e) {
            img.setText("No Image");
            img.setFont(new Font("Arial", Font.BOLD, 14));
            img.setForeground(Color.LIGHT_GRAY);
        }

        JLabel name = new JLabel(p.name);
        name.setFont(new Font("Arial", Font.BOLD, 16));
        name.setForeground(TEXT_WHITE);
        name.setAlignmentX(CENTER_ALIGNMENT);

        JLabel ccLbl = new JLabel(p.cc);
        ccLbl.setFont(new Font("Arial", Font.PLAIN, 12));
        ccLbl.setForeground(Color.LIGHT_GRAY);
        ccLbl.setAlignmentX(CENTER_ALIGNMENT);

        JLabel price = new JLabel(String.format("%.0f TK", p.price));
        price.setFont(new Font("Arial", Font.BOLD, 14));
        price.setForeground(ACCENT);
        price.setAlignmentX(CENTER_ALIGNMENT);

        JButton btn = createButton("Add to Cart", BG_MAIN, TEXT_WHITE);
        btn.setAlignmentX(CENTER_ALIGNMENT);
        btn.setBorder(new LineBorder(ACCENT));
        btn.addActionListener(e -> addToCart(p));

        card.add(Box.createVerticalStrut(15));
        card.add(img);
        card.add(Box.createVerticalStrut(10));
        card.add(name);
        card.add(Box.createVerticalStrut(2));
        card.add(ccLbl);
        card.add(Box.createVerticalStrut(5));
        card.add(price);
        card.add(Box.createVerticalStrut(10));
        card.add(btn);
        card.add(Box.createVerticalStrut(15));
        return card;
    }

    private JPanel createAdminPanel() {
        JPanel adminPanel = new JPanel();
        adminPanel.setBackground(new Color(40, 40, 40));
        adminPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(ACCENT), "ADMIN CONSOLE", 0, 0, new Font("Arial", Font.BOLD, 12), ACCENT));
        adminPanel.setPreferredSize(new Dimension(100, 100));

        nameInput = new JTextField(10);
        priceInput = new JTextField(7);
        ccInput = new JTextField(6);
        imageInput = new JTextField(8);

        JButton addBtn = createButton("ADD BIKE", new Color(0, 153, 76), TEXT_WHITE);
        JButton removeBtn = createButton("REMOVE", BTN_RED, TEXT_WHITE);
        JButton editBtn = createButton("UPDATE PRICE", Color.ORANGE, Color.BLACK);

        JLabel l1 = new JLabel("Bike Name:"); l1.setForeground(TEXT_WHITE);
        JLabel l2 = new JLabel("Price:"); l2.setForeground(TEXT_WHITE);
        JLabel l3 = new JLabel("CC:"); l3.setForeground(TEXT_WHITE);
        JLabel l4 = new JLabel("Img (Optional):"); l4.setForeground(TEXT_WHITE);

        adminPanel.add(l1); adminPanel.add(nameInput);
        adminPanel.add(l2); adminPanel.add(priceInput);
		adminPanel.add(l3); adminPanel.add(ccInput);
        adminPanel.add(l4); adminPanel.add(imageInput);
        
        adminPanel.add(Box.createHorizontalStrut(10));
        adminPanel.add(addBtn);
        adminPanel.add(editBtn);
        adminPanel.add(removeBtn);

        addBtn.addActionListener(e -> {
            try {
                String n = nameInput.getText();
                double p = Double.parseDouble(priceInput.getText());
                String c = ccInput.getText() + " cc";
                String imgName = imageInput.getText(); 
                
                String finalPath = "";
                if (imgName.isEmpty()) {
                    finalPath = "no_image"; 
                } else {
                    finalPath = "./Images/" + imgName; 
                }

                if(!n.isEmpty()) {
                    inventory.add(new Product(n, p, finalPath, c));
                    refreshProductGrid();
                    JOptionPane.showMessageDialog(this, "Added!");
                    nameInput.setText(""); priceInput.setText(""); ccInput.setText(""); imageInput.setText("");
                }
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Check Inputs"); }
        });

        removeBtn.addActionListener(e -> {
            inventory.removeIf(p -> p.name.equalsIgnoreCase(nameInput.getText()));
            refreshProductGrid();
            JOptionPane.showMessageDialog(this, "Removed if existed");
        });

        editBtn.addActionListener(e -> {
           for(Product p : inventory) {
               if(p.name.equalsIgnoreCase(nameInput.getText())) {
                   p.price = Double.parseDouble(priceInput.getText());
               }
           }
           refreshProductGrid();
        });

        return adminPanel;
    }

    private JPanel createCartSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(400, 0));
        sidebar.setBackground(BG_PANEL);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Color.GRAY));

        JLabel cartHeader = new JLabel("YOUR CART", SwingConstants.CENTER);
        cartHeader.setFont(new Font("Arial", Font.BOLD, 22));
        cartHeader.setForeground(TEXT_WHITE);
        cartHeader.setBorder(new EmptyBorder(20, 0, 20, 0));
        sidebar.add(cartHeader, BorderLayout.NORTH);

        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(BG_PANEL);
        JScrollPane scroll = new JScrollPane(cartItemsPanel);
        scroll.setBorder(null);
        sidebar.add(scroll, BorderLayout.CENTER);

        JPanel footer = new JPanel(new GridLayout(3, 1, 10, 10));
        footer.setBackground(BG_PANEL);
        footer.setBorder(new EmptyBorder(20, 20, 30, 20));

        totalAmountLabel = new JLabel("Total: 0 TK", SwingConstants.CENTER);
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 24));
        totalAmountLabel.setForeground(ACCENT);

        JButton payBtn = createButton("CONFIRM PAYMENT", ACCENT, Color.BLACK);
        payBtn.addActionListener(e -> saveReceipt());

        JButton clearBtn = createButton("CLEAR CART", BTN_RED, TEXT_WHITE);
        clearBtn.addActionListener(e -> { cart.clear(); updateCart(); });

        footer.add(totalAmountLabel);
        footer.add(payBtn);
        footer.add(clearBtn);
        sidebar.add(footer, BorderLayout.SOUTH);

        return sidebar;
    }

    private void addToCart(Product p) {
        boolean found = false;
        for (CartItem item : cart) {
            if (item.product.name.equals(p.name)) {
                item.quantity++;
                found = true;
                break;
            }
        }
        if (!found) cart.add(new CartItem(p, 1));
        updateCart();
    }

    private void updateCart() {
        cartItemsPanel.removeAll();
        double total = 0.0;

        if (cart.isEmpty()) {
            JLabel empty = new JLabel("Cart is empty");
            empty.setForeground(Color.GRAY);
            empty.setAlignmentX(CENTER_ALIGNMENT);
            cartItemsPanel.add(Box.createVerticalStrut(50));
            cartItemsPanel.add(empty);
        } else {
            for (int i = 0; i < cart.size(); i++) {
                final int idx = i;
                CartItem item = cart.get(i);
                total += item.product.price * item.quantity;

                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(new Color(60, 60, 65));
                row.setMaximumSize(new Dimension(380, 80));
                row.setBorder(new EmptyBorder(10, 10, 10, 10));

                JPanel info = new JPanel();
                info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
                info.setOpaque(false);
                
                JLabel n = new JLabel(item.product.name);
                n.setForeground(Color.WHITE);
                n.setFont(new Font("Arial", Font.BOLD, 14));
                
                JLabel p = new JLabel(item.quantity + " x " + item.product.price);
                p.setForeground(Color.LIGHT_GRAY);

                info.add(n); info.add(p);

                JPanel ctrls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                ctrls.setOpaque(false);
                
                JButton plus = createMiniButton("+");
                plus.addActionListener(e -> { item.quantity++; updateCart(); });
                
                JButton minus = createMiniButton("-");
                minus.addActionListener(e -> { if(item.quantity>1) item.quantity--; updateCart(); });
                
                JButton del = createMiniButton("X");
                del.setBackground(BTN_RED);
                del.addActionListener(e -> { cart.remove(idx); updateCart(); });

                ctrls.add(minus); ctrls.add(plus); ctrls.add(del);

                row.add(info, BorderLayout.WEST);
                row.add(ctrls, BorderLayout.EAST);
                cartItemsPanel.add(row);
                cartItemsPanel.add(Box.createVerticalStrut(10));
            }
        }

        totalAmountLabel.setText("Total: " + String.format("%.0f", total) + " TK");
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    private void saveReceipt() {
        if(cart.isEmpty()) return;
        try (PrintWriter writer = new PrintWriter(new FileWriter("Receipt.txt", true))) {
            writer.println("RECEIPT");
            for (CartItem item : cart) {
                writer.println(item.product.name + " x" + item.quantity);
            }
            writer.println("TOTAL: " + totalAmountLabel.getText());
            writer.println("");
            JOptionPane.showMessageDialog(this, "Receipt Saved!");
            cart.clear();
            updateCart();
        } catch (Exception e) { }
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        return btn;
    }

    private JButton createMiniButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.DARK_GRAY);
        btn.setForeground(Color.WHITE);
        btn.setMargin(new Insets(2, 8, 2, 8));
        return btn;
    }
}