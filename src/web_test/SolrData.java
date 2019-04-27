package web_test;
import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.Cluster;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.ClusteringResponse;
import org.apache.solr.common.SolrDocumentList;

/**
 * Created by DIAN on 2019/4/27.
 */
public class SolrData {
    public String getData(String id){

        System.out.println("hello:"+id);
//    getTxtFromTextFile(id);
        String urlString = "http://localhost:8983/solr/test";
        SolrClient solr = new HttpSolrClient.Builder(urlString).build();
        System.out.println("solr");
        //使用这个对象做查询
        SolrQuery params = new SolrQuery();
        //查询所有数据
        params.set("qt", "/clustering");
        params.setQuery("*:*");
        params.setStart(0);
        params.setRows((int)getDocTotalCount(solr));

        try {
            QueryResponse queryResponse = solr.query(params);
            ClusteringResponse clr = queryResponse.getClusteringResponse();
            List<Cluster> list = clr.getClusters();
            //拿到聚类数据集合,返回查询结果

            String txt = "";
            for (Cluster c : list) {
                //类别标签
                List<String> lblist = c.getLabels();
                for (String lb : lblist) {
                    System.out.println(lb);
                }
                //聚类文档ID
                List<String> doclist = c.getDocs();
                for (String doc : doclist) {
                    System.out.println("        " + doc);
                }
            }
        }
        catch (SolrServerException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return "getdata";
    }
    /**
     * @Author：sks
     * @Description：获取文档总数
     * @Date：
     */
    private  long getDocTotalCount(SolrClient solr ) {
        long num = 0;
        try {
            SolrQuery params = new SolrQuery();
            params.set("q", "*:*");
            //params.setQuery("*:*");
            QueryResponse rsp = solr.query(params);
            SolrDocumentList docs = rsp.getResults();
            num = docs.getNumFound();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return num;
    }
}
