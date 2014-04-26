package javafxapp.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import javafxapp.adapter.fns.FNS;
import javafxapp.crypto.WSSTool;
import javafxapp.utils.Mapper;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuilderRequest {


    private static Mapper innerMapper = new Mapper();

   /* public static Adapter createAdapter() throws IOException {
        Adapter adapter = new Adapter();
        adapter.setRequest(IOUtils.toString(BuilderRequest.class.getResourceAsStream("javafxapp.adapter.request.ftl"), "UTF-8"));

        return adapter;
    }*/

    public static List<String> buildRequestByTemplate(List<FNS> fnsList)  {
        Configuration cfg = new Configuration();
        List<String> requests = new ArrayList<>();
        try {
            Template template = cfg.getTemplate("src/main/java/javafxapp/adapter/fns/request.ftl");

        /*SmevFields SmevFields = (SmevFields) innerMapper.fillSmevPojo(null, new FNS());
        fns.setIsInn("on");
        List<String> listInn = new ArrayList<>();
        listInn.add("500100732259");
        fns.setInn(listInn);*/
            StringWriter out = null;
            for (FNS pojo : fnsList) {
                Field[] fields = pojo.getClass().getFields();
                Map<String, Object> map = new HashMap<String, Object>();
                for (Field f : fields) {
                    map.put(f.getName(), f.get(pojo));
                }
                out = new StringWriter();
                template.process(map, out);
                String result = new String(out.getBuffer().toString().getBytes("UTF-8"), "UTF-8");
                out.flush();
                out.close();
                result = WSSTool.signSoapRequest(result);

                requests.add(result);
            }
        }catch (Exception e){
            throw new RuntimeException(e.getLocalizedMessage());
        }

        return requests;
    }

    public static FNS fillSmevFieldsDefault() throws Exception {
        Class<?> pojoObject = Class.forName("javafxapp.adapter.fns.FNS");
        FNS pojo = (FNS) pojoObject.newInstance();
        pojo = (FNS) innerMapper.fillSmevPojo(null, pojo);
        return pojo;
    }

}