package cadlib;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Variant;

public class CadLibMain {
    public static void main(String[] args) {
        System.out.println(" --- Start CadLibMain --- ");
        System.setProperty("jna.platform.library.path", "/opt/portal/libs/linux/");
        cadLib = CadLib.INSTANCE;
        CadLibMain.run();
        System.out.println(" --- End CadLibMain     --- ");
    }

    private static String fileName = "/opt/portal/libs/linux/Gasket.dwg";
    private static Pointer CadImage = null;
    private static cadlib.CadLib cadLib = null;

    private static void run() {
        for (int i = 0; i < 1500; i++) {
            System.out.println(" --- begin --- " + i);
            CadLibMain.createImage();
            CadLibMain.runExportPng();
            // CadLibMain.runExportJpeg2();
            // CadLibMain.runExportSvg();
            CadLibMain.cadClose();
            System.out.println(" --- end --- " + i);
        }
    }

    private static void createImage() {
        CadImage = cadLib.CreateCAD(null, fileName);
    }

    private static void runExportPng() {
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<cadsofttools version=\"2\">\n" +
                "    <!-- Description:  Returns picture of the current view in Base64 encoding. -->\n" +
                "  <getimage>\n" +
                "      <ExportParams FileName = \"%SGDRAWINGSPATH%\\Image\" Format=\".png\"><!-- attribute 'FileName' calls additional saving to file -->\n" +
                "         <Width>1024</Width><!--always needs setup  -->\n" +
                "         <Height>768</Height><!--always needs setup -->\n" +
                "         <Proportional>True</Proportional>\n" +
                "         <BitPerPixel>24</BitPerPixel>\n" +
                "         <MeasureInPixels>True</MeasureInPixels>\n" +
                "         <Transparent>True</Transparent>\n" +
                "         <BackgroundColor>$FFFFFF</BackgroundColor><!--default value white -->\n" +
                "         <DefaultColor>$000000</DefaultColor><!--default value black -->\n" +
                "         <ClipMode>4</ClipMode>\n" +
                "         <ClipRect>0,0,0,2048,1536,0</ClipRect>\n" +
                "         <LayoutNameExportMode>Layout Bud2</LayoutNameExportMode><!--default current layout. The first char '@' of this name is layout index -->\n" +
                "      </ExportParams>\n" +
                "    </getimage>\n" +
                "</cadsofttools>";
        Variant.VARIANT var = new Variant.VARIANT();
        int result = cadLib.ProcessXML(CadImage, input, var.getPointer());
    }

    private static void runExportJpeg2() {
        String params =
                "      <ExportParams FileName = \"%SGDRAWINGSPATH%\\Image\" Format=\".jpeg\"><!-- attribute 'FileName' calls additional saving to file -->\n" +
                        "     <Width>300</Width>\n" +
                        "     <Height>-1</Height>\n" +
                        "     <Proportional>True</Proportional>\n" +
                        "     <BitPerPixel>24</BitPerPixel>\n" +
                        "     <Quality>100</Quality>\n" +
                        "     <MeasureInPixels>True</MeasureInPixels>\n" +
                        "     <DPUX>96</DPUX>\n" +
                        "     <DPUY>96</DPUY>\n" +
                        "     <Compression>LZW</Compression>\n" +
                        "</ExportParams>";
        CadLib.INSTANCE.SaveCADWithXMLParametrs(CadImage, params);
    }

    private static void runExportSvg() {
        String params = "<ExportParams FileName = \"%SGDRAWINGSPATH%\\ImageSvg\" Format=\".svg\"><!-- attribute 'FileName' calls additional saving to file -->\n" +
                "</ExportParams>";
        CadLib cadLib = CadLib.INSTANCE;
        cadLib.SaveCADWithXMLParametrs(CadImage, params);
    }

    private static void cadClose() {
        cadLib.CADClose(CadImage);
        CadImage = null;
    }
}
