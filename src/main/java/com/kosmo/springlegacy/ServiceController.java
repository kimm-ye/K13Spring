package com.kosmo.springlegacy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import autoscan.LottoService;
import autoscan.LottoVO;
import autoscan.MyService;

/*
service-context.xml에서 component-scan으로 설정된 기본패키지에서
컨트롤러 클래스 역할을 부여하기 위한 어노테이션으로, 스프링 컨테이너가 시작될때
자동으로 빈이 생성된다.
 */
@Controller
public class ServiceController {

	/*
	서비스 객체를 주입받는다.
	@Autowired는 멤버변수, 생성자, setter에서 사용할 수 있다.
	또한 미리 생성되어 있어야하고, 그렇지 않으면 서버가
	정상적으로 실행되지 않는다.
	
	new를 사용하지 않고 아래와 같이 만들 수 있다.
	 */
	MyService myService;
	
	//이미 만들어져있는 것만 @Autowired로 받을 수 있다.
	@Autowired
	public void setMyService(MyService myService) {
		this.myService = myService;
		System.out.println("setMyService()호출-ServiceController");
	}
	
	/*
	컨트롤러는 요청을 분석한 후 적적한 서비스 객체를 호출한다.
	이때 컨트롤러가 받은 모든 요청(request객체)을 전달한다.
	 */
	@RequestMapping("/service/myService.do")
	public String myService() {
		myService.execute();
		return "07Service/myService";
	}
	
	
	/*
	서비스 객체의 호출을 위해 빈을 자동주입 받는다.
	일반적으로 setter 메서드 없이 멤버변수에 직접 어노테이션을 부착한다.
	 */
	@Autowired
	private LottoService lottoService;
	
	/*
	사용자가 HTML페이지에서 각 링크를 클릭할때 전달하는 userLottoNum 파라미터를
	lottoVO 객체가 받아서 사용한다.
	 */
	@RequestMapping("/service/myLotto.do")
	public String myLotto(LottoVO lottoVO, Model model) {
		
		/*
		파라미터로 전달되는 값은 커맨드객체(VO객체)를 통해 한꺼번에 받을 수 있으므로
		getParameter()가 필요없다.
		 */
		lottoVO = lottoService.getLottoProcess(lottoVO.getUserLottoNum(), lottoVO);
		
		return "07Service/myLotto";
	}
}
