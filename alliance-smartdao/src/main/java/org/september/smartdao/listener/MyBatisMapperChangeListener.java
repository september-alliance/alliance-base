package org.september.smartdao.listener;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.september.core.util.JVMUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;


/**
 * 修改mapper文件不重启
 * longqiong,yexinzhou
 */
@Component
public class MyBatisMapperChangeListener implements InitializingBean, ApplicationContextAware {
    private volatile ConfigurableApplicationContext context = null;
    private volatile Scanner scanner = null;

    private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(!JVMUtil.isDebug()) {
            return;
        }
        scanner = new Scanner();
        //新开辟一个线程监控mapper文件是否修改，新开辟线程是为了防止 watcher.take() 阻塞程序启动
        new Thread(new watchMapper()).start();
    }

    class Scanner {
        //mapper资源目录，mapper文件在jdt-dao.jar包中，监控不方便，不采用此种方式
//        private static final String XML_RESOURCE_PATTERN = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "mapper/*.xml";
        // CommonEntityMapper.xml路径，因removeConfig 会清除Configuration中的CommonEntityMapper.xml 信息，故需重新加载
        private static final String COMMONENTITYMAPPER_RESOURCE_PATTERN = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "CommonEntityMapper.xml";
        
        public Scanner(){}

        /**
         * 重新加载mapper
         * @throws Exception
         */
        public void reloadXML() throws Exception {
            SqlSessionFactory factory = context.getBean(SqlSessionFactory.class);
            Configuration configuration = factory.getConfiguration();
            List<String> dirs = getMapperDir();
            removeConfig(configuration);
            for (Resource resource : findResource(dirs)) {
                try {
                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resource.getInputStream(), configuration, resource.toString(), configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                } finally {
                    ErrorContext.instance().reset();
                }
            }

            for (Resource resource : findCommonEntityMapper()) {
                try {
                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resource.getInputStream(), configuration, resource.toString(), configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                } finally {
                    ErrorContext.instance().reset();
                }
            }
        }

        /**
         * 移除Configuration中的mapper信息(mybatis的所有mapper信息保存在Configuration的字段中)
         * @param configuration
         * @throws Exception
         */
        private void removeConfig(Configuration configuration) throws Exception {
            Class<?> classConfig = configuration.getClass();
            clearStatments(configuration);
//            clearMap(classConfig, configuration, "mappedStatements");
            clearMap(classConfig, configuration, "caches");
            clearMap(classConfig, configuration, "resultMaps");
            clearMap(classConfig, configuration, "parameterMaps");
            clearMap(classConfig, configuration, "keyGenerators");
            clearMap(classConfig, configuration, "sqlFragments");
            clearSet(classConfig, configuration, "loadedResources");
        }

        private void clearStatments(Configuration configuration) throws Exception{
        	 Field field = Configuration.class.getDeclaredField("mappedStatements");
             field.setAccessible(true);
        	 Map mapper = (Map) field.get(configuration);
        	 List targetList = new ArrayList();
             for(Object key : mapper.keySet()) {
             	MappedStatement ms = (MappedStatement)mapper.get(key);
             	if(!ms.getId().startsWith("CommonEntityMapper")) {
             		targetList.add(key);
             	}
             }
             
             for(Object key : targetList) {
            	 mapper.remove(key);
             }
        }
        private void clearMap(Class<?> classConfig, Configuration configuration, String fieldName) throws Exception {
            Field field = classConfig.getDeclaredField(fieldName);
            field.setAccessible(true);
            ((Map) field.get(configuration)).clear();
        }
        private void clearSet(Class<?> classConfig, Configuration configuration, String fieldName) throws Exception {
            Field field = classConfig.getDeclaredField(fieldName);
            field.setAccessible(true);
            ((Set) field.get(configuration)).clear();
        }
        
        private List<Resource> findResource(List<String> dirs) throws IOException {
            List<Resource> resources = new ArrayList<Resource>();
            for(String path : dirs){
                resources.addAll(findResource(path));
            }
            return resources;
        }

        /**
         * 获取mapper文件
         * @return
         * @throws IOException
         */
        private List<Resource> findResource(String path) throws IOException {
            File dir = new File(path);
            Collection<File> xmlList = FileUtils.listFiles(dir, new String[]{"xml"}, true);

            List<Resource> arraySource = new ArrayList<Resource>();
            
            for(File xml : xmlList){
                arraySource.add(new FileSystemResource(xml));
            }
            return arraySource;
        }

        /**
         * 获取CommonEntityMapper
         * @return
         * @throws IOException
         */
        private Resource[] findCommonEntityMapper() throws IOException {
            return resourcePatternResolver.getResources(COMMONENTITYMAPPER_RESOURCE_PATTERN);
        }
    }

    private List<String> getMapperDir(){
        SqlSessionFactory factory = context.getBean(SqlSessionFactory.class);
        Configuration configuration = factory.getConfiguration();
        //get loadedResources
        List<String> dirs = new ArrayList<String>();
        try{
            Field field = configuration.getClass().getDeclaredField("loadedResources");
            field.setAccessible(true);
            Set resources = (Set)field.get(configuration);
            if(resources.isEmpty()){
                System.out.println("---------falid to find mytabis mapper path-----------");
                return dirs;
            }
            Iterator it = resources.iterator();
            while(it.hasNext()){
                String path = (String) it.next();
                if("resource loaded through InputStream".equals(path)){
                    //path = (String) it.next();
                    continue;
                }
                if(path.contains("CommonEntityMapper.xml")){
                    continue;
                }
                String result = path.split("mapper")[0].split("\\[")[1]+"mapper";
                if(!dirs.contains(result)){
                    dirs.add(result);
                    System.out.println("---------find mytabis mapper path-----------");
                    System.out.println("---------"+result+"-----------");
                }
                
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return dirs;
    }
    private Collection<File> getWatchDirs(){
        //只要目录
        try{
            Collection<File> dirList = new ArrayList<File>();
            for(String rootDir : getMapperDir()){
                Collection<File> dirs = FileUtils.listFilesAndDirs(new File(rootDir),FalseFileFilter.INSTANCE,TrueFileFilter.INSTANCE);
                dirList.addAll(dirs);
            }
            return dirList;
        }catch(Exception ex){
            System.out.println("获取mapper文件目录失败，如果你正在使用tomcat调试，请使用jetty即可。");
            return new ArrayList<File>();
        }
        
    }
    /**
     * 监听mapper修改
     */
    class watchMapper implements Runnable{
        @Override
        public void run() {
            WatchService watcher = null;
            try {
                //注册服务
                watcher = FileSystems.getDefault().newWatchService();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Collection<File> dirs = getWatchDirs();
                for(File dir : dirs){
                    //只监听修改
                    Paths.get(dir.getAbsolutePath()).register(watcher,StandardWatchEventKinds.ENTRY_MODIFY);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                WatchKey key = null;
                try {
                    key = watcher.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                List<WatchEvent<?>> events = key.pollEvents();
                //如果监控到mapper文件修改事件 poll出事件
                for (WatchEvent<?> event: events) {
                    if(event.kind().equals(StandardWatchEventKinds.ENTRY_MODIFY)){
                        System.out.println(event.context() + "被修改");
                        if(event.context().toString().endsWith(".xml")){
                            System.out.println("reload mapper");
                            try {
                                //有mapper文件修改，重新加载mapper文件
                                scanner.reloadXML();
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        }
    }
}