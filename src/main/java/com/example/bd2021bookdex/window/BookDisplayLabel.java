package com.example.bd2021bookdex.window;

import com.example.bd2021bookdex.database.entities.AuthorEntity;
import com.example.bd2021bookdex.database.entities.BookEntity;
import com.example.bd2021bookdex.database.entities.TagEntity;
import com.example.bd2021bookdex.database.entities.UserEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;

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
import java.util.concurrent.ExecutionException;

public class BookDisplayLabel extends JPanel {
    private static final int GBC_I = 3;
    private static ImageIcon unknownIcon = null;

    static {
        try {
            unknownIcon = new ImageIcon(resize(ImageIO.read(new File("placeHolder.png")),100,100));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BookStatusEntity book;
    private JLabel titleLabel = new JLabel();
    private JLabel authorLabel = new JLabel();
    private JLabel picture = new JLabel();
    private java.util.List<JLabel> tags = new LinkedList<>();
    private JTextArea desc = new JTextArea();
    private JScrollPane scroll;
    private JLabel pageCount = new JLabel();
    private JLabel category = new JLabel();
    private JLabel genre = new JLabel();
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
        scroll.setMinimumSize(new Dimension(x / 5*3, y*2/5));
        scroll.setPreferredSize(new Dimension(x / 5*3, y*2/5));
        scroll.setBorder(null);
        add(scroll, gbc);
        
        
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(pageCount, gbc);
        
        gbc.gridx = 2;
        add(category, gbc);
        
        gbc.gridx = 3;
        add(genre, gbc);
        
        setValues();
        if (book.isOwned()) {
            this.setBackground(new Color(255, 223, 0));
        }
        else {
            this.setBackground(Color.white);
        }
    }
    private void setValues() {
        if (book.getStatus() == BookStatusEnum.READ) {
            desc.setBackground(new Color(255,223,0));
            scroll.getVerticalScrollBar().setUI(new ScrollBarUI(new Color(255, 223, 0)));
        }
        if (book.isAtLeastAdded())
            titleLabel.setText(book.getBook().getTitle());
        else
            titleLabel.setText(book.getBook().getTitle().replaceAll("\\S", "?"));
        Set<AuthorEntity> authors = book.getBook().getAuthors();
        StringBuilder names = new StringBuilder();
        for (AuthorEntity a : authors) {
          
            if (authorLabel.getFontMetrics(authorLabel.getFont()).stringWidth(names.toString()) +
                    authorLabel.getFontMetrics(authorLabel.getFont()).stringWidth(a.getName()) > this.getPreferredSize().width) {
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
        if (book.getBook().getDesc() != null)
            desc.setText(book.getBook().getDesc());
        if (book.getBook().getCategory() != null)
            category.setText(book.getBook().getCategory());
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
        }
        int i = 0;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 5;
        for (JLabel l : tags) {
            gbc.gridx = i++;
            add(l, gbc);
        }
        
    }
    public BookStatusEntity getBook() {
        return book;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (loader != null && loader.isDone()) {
            BufferedImage image;
            try {
                image = loader.get();
            if (book.getStatus() == BookStatusEnum.ADDED)
                picture.setIcon(new ImageIcon(dye(resize(image,50,100))));
            else
                picture.setIcon(new ImageIcon(resize(image,50,100)));

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
