package com.example.bd2021bookdex.window.middlepanel;

import com.example.bd2021bookdex.database.DatabaseModifier;
import com.example.bd2021bookdex.database.DatabaseSearcher;
import com.example.bd2021bookdex.database.entities.AuthorEntity;
import com.example.bd2021bookdex.database.entities.TagEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEnum;
import com.example.bd2021bookdex.window.*;
import com.example.bd2021bookdex.window.middlepanel.popup.ManageCollectionWindow;
import com.example.bd2021bookdex.window.middlepanel.popup.ManageDetailsWindow;
import com.example.bd2021bookdex.window.middlepanel.popup.ManageStatusWindow;
import com.example.bd2021bookdex.window.ui.MyButton;
import com.example.bd2021bookdex.window.ui.ScrollBarUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
@Scope("prototype")
public class BookDisplayLabel extends JPanel {
    private static final int GBC_I = 3;
    private static ImageIcon unknownIcon = null;

    static {
        try {
            unknownIcon = new ImageIcon(resize(ImageIO.read(new File("placeHolder.png")),75,100));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private ApplicationContext context;
    @Autowired
    private MainWindow owner;
    @Autowired
    private DatabaseSearcher searcher;
    @Autowired
    private DatabaseModifier modifier;
    private BookStatusEntity book;
    private final JLabel titleLabel = new JLabel();
    private final JLabel authorLabel = new JLabel();
    private final JLabel picture = new JLabel();
    private final java.util.List<JLabel> tags = new LinkedList<>();
    private final JTextArea desc = new JTextArea();
    private final JScrollPane scroll;
    private final JLabel pageCount = new JLabel();
    private final JLabel category = new JLabel();
    private final JLabel genre = new JLabel();
    private CompletableFuture<BufferedImage> loader = null;
    
    public BookDisplayLabel(BookStatusEntity src, int x, int y) {
        this.setMinimumSize(new Dimension(x,y));
        this.setPreferredSize(new Dimension(x,y));
        this.setMaximumSize(new Dimension(x,y));
        this.setBorder(new LineBorder(Color.black,1));
        book = src;
        setBackground(Color.white);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2,2,2,2);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        add(titleLabel, gbc);
        
        gbc.gridy = 1;
        add(authorLabel, gbc);
        
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        add(picture, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.gridheight = 2;
        desc.setLineWrap(true);
        desc.setEditable(false);
        desc.setWrapStyleWord(true);
        scroll = new JScrollPane(desc);
        scroll.getVerticalScrollBar().setUI(new ScrollBarUI(Color.white));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setMinimumSize(new Dimension(x / 2, y*2/5));
        scroll.setPreferredSize(new Dimension(x / 2, y*2/5));
        scroll.setBorder(null);
        add(scroll, gbc);
        
        addAdditionalLabels();
        
        setValues();
        
        addButtons();
    }
    
    private void addAdditionalLabels() {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(pageCount, gbc);
        pageCount.setMinimumSize(new Dimension(100,20));
        pageCount.setPreferredSize(new Dimension(100,20));
        pageCount.setMaximumSize(new Dimension(100,20));

        gbc.gridx = 2;
        add(category, gbc);
        category.setMinimumSize(new Dimension(75,20));
        category.setPreferredSize(new Dimension(75,20));
        category.setMaximumSize(new Dimension(75,20));

        gbc.gridx = 3;
        add(genre, gbc);
        genre.setMinimumSize(new Dimension(75,20));
        genre.setPreferredSize(new Dimension(75,20));
        genre.setMaximumSize(new Dimension(75,20));
    }
    
    private void addButtons() {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        MyButton toAdd = new MyButton("Change status");
        add(toAdd, gbc);
        toAdd.addActionListener(actionEvent -> {
            var dialog = context.getBean(ManageStatusWindow.class,this, book, owner, modifier);
        });
        toAdd.setFont(toAdd.getFont().deriveFont(9.55f));

        gbc.gridy = 2;

        toAdd = new MyButton("Tags & Genre");
        add(toAdd, gbc);
        toAdd.addActionListener(actionEvent -> {
            var dialog = context.getBean(ManageDetailsWindow.class,this, book.getBook(), owner, searcher, modifier);
        });
        toAdd.setFont(toAdd.getFont().deriveFont(9.55f));

        gbc.gridy = 4;

        toAdd = new MyButton("Collections");
        add(toAdd, gbc);
        toAdd.addActionListener(actionEvent -> {
            var dialog = context.getBean(ManageCollectionWindow.class,book, owner, searcher, modifier);
        });
        toAdd.setFont(toAdd.getFont().deriveFont(9.55f));
    }
    
    private void setValues() {
        
        if (book.getStatus() == BookStatusEnum.READ) {
            desc.setBackground(new Color(255,223,0));
            scroll.getVerticalScrollBar().setUI(new ScrollBarUI(new Color(255, 223, 0)));
        }
        setTitleAndAuthorNames();

        startPictureDownload();
        
        setAdditionalLabels();
        
        int i = 0;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 5;
        for (JLabel l : tags) {
            gbc.gridx = i++;
            add(l, gbc);
        }
        if (book.isOwned()) {
            this.setBackground(new Color(255, 223, 0));
        }
        else {
            this.setBackground(Color.white);
        }
    }
    
    private void setTitleAndAuthorNames() {
        String toAdd = book.getTitle();

        if (toAdd.length() > 40) {
            toAdd = toAdd.substring(0,37) + "...";
        }
        titleLabel.setText(toAdd);
        Set<AuthorEntity> authors = book.getBook().getAuthors();
        StringBuilder names = new StringBuilder();
        for (AuthorEntity a : authors) {

            if (authorLabel.getFontMetrics(authorLabel.getFont()).stringWidth(names.toString()) +
                    authorLabel.getFontMetrics(authorLabel.getFont()).stringWidth(a.getName()) > this.getPreferredSize().width /3 * 2) {
                names.append(".....");
                break;
            }
            names.append(a.getName());
            names.append(", ");
        }
        if (names.length() > 0) {
            names.deleteCharAt(names.length() - 1);
            names.deleteCharAt(names.length() - 1);
        }
        authorLabel.setText(names.toString());
    }
    
    private void startPictureDownload() {
        try {
            if (book.getStatus() == BookStatusEnum.FOUND)
                throw new Exception();
            if (book.getBook().getLink() == null)
                throw new NullPointerException();

            loader = CompletableFuture.supplyAsync(() -> {
                try {
                    return ImageIO.read(new URL(book.getBook().getLink()));
                } catch (IOException e) {
                    return null;
                }
            });
        } catch(Exception e) {
            try {
                picture.setIcon(unknownIcon);
            } catch (Exception ignored){}

        }
    }
    
    private void setAdditionalLabels() {
        if (book.getBook().getDesc() != null)
            desc.setText(book.getBook().getDesc());
        if (book.getBook().getCategory() != null) {
            String toAdd = book.getBook().getCategory();
            if (toAdd.length() > 15) {
                toAdd = toAdd.substring(0,13) + "...";
            }
            category.setText(toAdd);
        }
        if (book.getBook().getGenre() != null)
            genre.setText(book.getBook().getGenre());
        if (book.getBook().getCount() != 0) {
            if (book.getStatus() == BookStatusEnum.FOUND)
                pageCount.setText("Pages: ???");
            else
                pageCount.setText("Pages: " + book.getBook().getCount());
        }
        for (TagEntity t: book.getBook().getTags()) {
            JLabel temp = new JLabel(t.getName());
            if (book.getStatus() == BookStatusEnum.READ)
                temp.setBackground(new Color(255,223,0));
            tags.add(temp);
            temp.setMinimumSize(new Dimension(75,20));
            temp.setPreferredSize(new Dimension(75,20));
            temp.setMaximumSize(new Dimension(75,20));
        }
    }
    
    public BookStatusEntity getBook() {
        return book;
    }
    
    public void update() {
        book = searcher.getUpdatedStatus(book);
        for (var label : tags) {
            remove(label);
        }
        tags.clear();
        setValues();
        this.revalidate();
        this.repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        if (loader != null && loader.isDone()) {
            BufferedImage image;
            try {
                image = loader.get();
            if (book.getStatus() == BookStatusEnum.ADDED)
                picture.setIcon(new ImageIcon(dye(resize(image,75,100))));
            else
                picture.setIcon(new ImageIcon(resize(image,75,100)));

            } catch (Exception e) {
                try {
                    picture.setIcon(unknownIcon);
                } catch (Exception ignored){}
            }
            loader = null;
        }
        super.paintComponent(g);
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
    private static BufferedImage dye(BufferedImage image)
    {
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(image, image);
        return image;
    }
}
