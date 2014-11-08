package me.kevingleason.nlp.server;


import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;


import me.kevingleason.nlp.server.pos.adt.TaggedWord;
import me.kevingleason.nlp.server.pos.app.RealTime;

/**
 * SwitchTagger is used to tag sentence parts of speech, potential use as an API in future.
 * @author Kevin Gleason
 */
public class SwitchTagger extends HttpServlet {
    //Requires a properties file to convert short-hand names to full. Properties file currently located on google drive.

    /**
     * The basic servlet function, tells how to handle an HTTP GET request and convert it into a log.
     * (Optional) writeHTML boolean can be used to bypass writing html to a page if using a TrackerC class and not
     * Tomcat web interface. Simply writes all data to logs.
     * @param request The http request received.
     * @param response The http get Response
     * @throws java.io.IOException
     * @throws ServletException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {


        String basePath = request.getContextPath();

        String question = request.getParameter("q");
        if(question==null || question.equals("")){
            PrintWriter out = response.getWriter();
            out.println("To use Tagger API, set value q= to a URL encoded word or sentence.");
            out.close();
        }
        else {
            List<List<String>> procSent = RealTime.processSentence(question);
            List<List<TaggedWord>> tagged = RealTime.tagWords(procSent);

            // Set the response MIME type of the response message
            response.setContentType("text/html");
            // Allocate a output writer to write the response message into the network socket
            PrintWriter out = response.getWriter();
            for (List<TaggedWord> tagSent : tagged) {
                out.println(formatOutput(tagSent));
            }

            // Write the log file using the writeLog function
            out.close();
        }
    }

    public static String formatOutput(List<TaggedWord> sentence){
        StringBuilder sb = new StringBuilder();
        for(TaggedWord w : sentence){
            sb.append(w.toString() + " ");
        }
        return sb.toString();
    }

    public static void main(String args[]) throws IOException{
    }
}