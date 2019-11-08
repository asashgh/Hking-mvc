import com.hgh.mvc.aop.Aop;
import com.hgh.mvc.ioc.Ioc;
import com.hgh.mvc.bean.BeanContainer;
import com.hgh.mvc.src.controller.HelloController;
import org.junit.Test;


public class IocTest {
    @Test
    public void test1(){
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("com.hgh.mvc.src");
        new Aop().doAop();
        new Ioc().doIoc();
        HelloController helloController = (HelloController) beanContainer.getBean(HelloController.class);
        helloController.hello();
        helloController.baga();
    }


}
