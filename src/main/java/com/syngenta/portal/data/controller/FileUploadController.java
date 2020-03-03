package com.syngenta.portal.data.controller;

import com.syngenta.portal.data.model.FileParseResults;
import com.syngenta.portal.data.service.DataLoadingService;
import com.syngenta.portal.data.service.MailService;
import com.syngenta.portal.data.service.ScriptExecutor;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class FileUploadController {
    @Autowired
    private DataLoadingService dataLoadingService;

    @Autowired
    private MailService mailService;

    @Autowired
    private ScriptExecutor scriptExecutor;

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        return "uploadForm";
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam(name = "mails",required = false) String mails
            , @RequestParam(name = "download", required = false) boolean download,
                                   RedirectAttributes redirectAttributes, HttpServletResponse response) {
        FileParseResults results = null;

        try {
            results = dataLoadingService.run(file.getBytes());
           // results.setEmailSentSuccessfully(mailService.sendEmail(mails, results));
            if (results.isParsingSucceed()) {
                results.setScriptApplied(scriptExecutor.execute(results.getScript()));
            }
            if (download) {
                zipDownload(response, results);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded & parsed " + file.getOriginalFilename() + "! , " +
                        "the parsing " + (results.isParsingSucceed() ? "Succeeded" : "Failed"));
        redirectAttributes.addFlashAttribute("mail",
                results.isEmailSentSuccessfully() ? "Mail is sent successfully" : "Mail failed to be sent");
        redirectAttributes.addFlashAttribute("scriptApplied", results.isScriptApplied() ? "Script applied successfully" : "Script failed");
        return "redirect:/";
    }

    private void zipDownload(HttpServletResponse response, FileParseResults results) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        if (results.isParsingSucceed()) {
            ZipEntry zipEntry = new ZipEntry("Script.sql");
            zipEntry.setSize(results.getScript().length());
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(
                    IOUtils.toByteArray(new FileInputStream(results.getScript())),
                    zipOut);
            zipEntry = new ZipEntry("Flat.txt");
            zipEntry.setSize(results.getFlatObjectScript().length());
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(
                    IOUtils.toByteArray(new FileInputStream(results.getFlatObjectScript())),
                    zipOut);
        } else {
            ZipEntry zipEntry = new ZipEntry("Error.txt");
            zipEntry.setSize(results.getErrorFile().length());
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(
                    IOUtils.toByteArray(new FileInputStream(results.getErrorFile())),
                    zipOut);
        }
        zipOut.closeEntry();

        zipOut.finish();
        zipOut.close();
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "download" + "\"");
    }

    @GetMapping(value = "/zip-download", produces = "application/zip")
    public void zipDownload(@RequestParam List<String> name, HttpServletResponse response) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        for (String fileName : name) {
            FileSystemResource resource = new FileSystemResource("fileBasePath" + fileName);
            ZipEntry zipEntry = new ZipEntry(resource.getFilename());
            zipEntry.setSize(resource.contentLength());
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(resource.getInputStream(), zipOut);
            zipOut.closeEntry();
        }
        zipOut.finish();
        zipOut.close();
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "download" + "\"");
    }


}
