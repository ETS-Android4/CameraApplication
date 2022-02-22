package com.example.myscreendesign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.util.List;

public class Tutorial extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {
    private static final String TAG=Tutorial.class.getSimpleName();
    private static final String FILE_NAME="android_tutorial.pdf";
    PDFView pdfView;
    Integer pageNumber=0;
    String pdfFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        pdfView=findViewById(R.id.pdf_view);
        displayFromAsset(FILE_NAME);

    }

    private void displayFromAsset(String fileName) {
        pdfFileName=fileName;
        pdfView.fromAsset(FILE_NAME).defaultPage(pageNumber).enableSwipe(true).
                swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true).
                onLoad(this).scrollHandle(new DefaultScrollHandle(this)).load();


    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber=page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));

    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta=pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");


    }

    private void printBookmarksTree(List<PdfDocument.Bookmark> tableOfContents, String s) {
        for (PdfDocument.Bookmark b : tableOfContents) {

            Log.e(TAG, String.format("%s %s, p %d", s, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), s + "-");
            }
        }
    }
    }


