package de.adv.guimaster.backend;

import java.io.File;
import java.util.HashMap;

import de.adv.guimaster.api.SerialPort;
import de.adv.guimaster.backend.ImageTracerAndroid;

public class ConvertSvg {

    public static  String convert() throws Exception{
        HashMap<String, Float> op = new HashMap<String, Float>();


        // Tracing
        op.put("ltres", 1f);
        op.put("qtres", 1f);
        op.put("pathomit", 8f);

        // Color quantization
        op.put("colorsampling", 1f); // 1f means true ; 0f means false: starting with generated palette
        op.put("numberofcolors", 16f);
        op.put("mincolorratio", 0.02f);
        op.put("colorquantcycles", 8f);

        // SVG rendering
        op.put("scale", 1f);
        op.put("roundcoords", 1f); // 1f means rounded to 1 decimal places, like 7.3 ; 3f means rounded to 3 places, like 7.356 ; etc.
        op.put("lcpr", 0f);
        op.put("qcpr", 0f);
        op.put("desc", 0f); // 1f means true ; 0f means false: SVG descriptions deactivated
        op.put("viewbox", 0f); // 1f means true ; 0f means false: fixed width and height

        // Selective Gauss Blur
        op.put("blurradius", 0f); // 0f means deactivated; 1f .. 5f : blur with this radius
        op.put("blurdelta", 50f); // smaller than this RGB difference will be blurred

        File file = SerialPort.file;
         return ImageTracerAndroid.imageToSVG(file.getAbsolutePath(), op, null);
    }
}
