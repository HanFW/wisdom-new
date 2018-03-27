/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import sessionBean.ArticleSessionBeanLocal;

/**
 *
 * @author Yongxue
 */
@Named(value = "addNewArticleManagedBean")
@RequestScoped
public class AddNewArticleManagedBean {

    /**
     * Creates a new instance of AddNewArticleManagedBean
     */
    @EJB
    private ArticleSessionBeanLocal articleSessionBeanLocal;

    private String articleTopic;
    private String artilceTitle;
    private String articleDescription;
    private String articleContent;

    private UploadedFile imageFile;
    private Long newArticleId;
    private Long authorId;

    private ExternalContext ec;

    public AddNewArticleManagedBean() {
    }

    public String getArticleTopic() {
        return articleTopic;
    }

    public void setArticleTopic(String articleTopic) {
        this.articleTopic = articleTopic;
    }

    public String getArtilceTitle() {
        return artilceTitle;
    }

    public void setArtilceTitle(String artilceTitle) {
        this.artilceTitle = artilceTitle;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public UploadedFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(UploadedFile imageFile) {
        this.imageFile = imageFile;
    }

    public void addNewArticle() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        authorId = Long.valueOf(ec.getSessionMap().get("authorId").toString());

        newArticleId = articleSessionBeanLocal.addNewArticle(articleTopic,
                artilceTitle, articleDescription, articleContent, authorId);
    }

    public void upload(FileUploadEvent event) throws IOException {
        imageFile = event.getFile();
        authorId = Long.valueOf(ec.getSessionMap().get("authorId").toString());

        if (imageFile != null) {
            String filename = authorId + ".png";

            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");
            OutputStream output = new FileOutputStream(new File(newFilePath, filename));

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = imageFile.getInputstream();

            while (true) {
                a = inputStream.read(buffer);
                if (a < 0) {
                    break;
                }
                output.write(buffer, 0, a);
                output.flush();
            }

            output.close();
            inputStream.close();
        }
    }
}
