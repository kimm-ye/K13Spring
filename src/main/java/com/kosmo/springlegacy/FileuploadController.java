package com.kosmo.springlegacy;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class FileuploadController {

	//파일업로드를 위한 디렉토리의 물리적 경로 확인하기
	@RequestMapping("/fileUpload/uploadPath.do")
	//request, response 내장객체를 사용하기 위해 매개변수로 선언
	public void uploadPath(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		
		//request 내장객체를 통해 서버의 물리적 경로 얻어옴
		String path = req.getSession().getServletContext().getRealPath("/resources/upload");
		//upload 디렉토리는 정적파일을 저장하기 위한 resources 하위에 생성한다.(Spring은 정적파일 저장 위치를 resources 밑으로 저장하기로 약속했으므로)
		
		//response 내장객체를 통해 MIME타입으로 설정한다.
		resp.setContentType("text/html; charset=utf-8");
		
		//View를 호출하지 않고, 컨트롤러에서 즉시 출력한다.
		PrintWriter pw = resp.getWriter();
		pw.print("/upload 디렉토리의 물리적경로 : " + path);
	}
	
	//파일 업로드 폼 매핑
	@RequestMapping("/fileUpload/uploadForm.do")
	public String uploadForm() {
		
		return "06FileUpload/uploadForm";
	}
	
	/*
	UUID(Universally Unique IDntifier)
		: 범용고유식별자. randomUUID()를 통해 문자열을 생성하면
		하이픈이 4개 포함된 32자의 랜덤하고 유니크한 문자열이 생성된다.
		JDK에서 기본 클래스로 제공된다.
	 */
	public static String getUuid() {
		String uuid = UUID.randomUUID().toString();
		System.out.println("생성된UUID-1 : " + uuid);
		
		uuid = uuid.replaceAll("-", "");
		System.out.println("생성된UUID-2 : " + uuid);
		
		return uuid;
	}
	
	/*
	파일업로드 처리
		: 파일업로드는 Post방식으로 전송해야 하므로 매핑시 method, value 두가지
		속성을 모두 기술해야 한다.
	 */
	@RequestMapping(method = RequestMethod.POST, value="/fileUpload/uploadAction.do")
	//매개변수가 두개 (= 두개를 모두 안에서 사용하겠다. model= 모델객체에 저장)
	//파일 업로드를 위한 객체를 매개변수로 선언한다.
	public String uploadAction(Model model, MultipartHttpServletRequest req) {
		
		//물리적경로 얻어오기
		String path = req.getSession().getServletContext().getRealPath("/resources/upload");
		MultipartFile mfile = null;
		
		//파일정보를 저장한 Map컬렉션을 2개 이상 저장하기 위한 용도의 List컬렉션
		List<Object> resultList = new ArrayList<Object>();
		
		//uploadAction 이 시작되자마자 파일은 업로드됨. 그러고 남은게 지금 title이라서 그걸 밑에서 처리함
		try {
			String title = req.getParameter("title");
			
			//업로드폼의 file속성의 필드를 가져온다. (여기서는 파일필드가 2개임)
			Iterator itr = req.getFileNames(); //Iterator는 반복객체
			
			//갯수만큼 반복
			while(itr.hasNext()) {
				//전송된 파일명을 읽어온다.
				mfile = req.getFile(itr.next().toString());
				
				//한글깨짐방지 처리 후 전송된 파일명을 가져온다. (String객체를 통해 한글처리를 한다.)
				String originalName = new String(mfile.getOriginalFilename().getBytes(),"UTF-8");
				
				//만약 originalName이 아무값도 없다면 실패했거나 파일이 없다면 continue 즉, 반복문의 처음으로 돌아감
				//서버로 전송된 파일이 없다면 while문의 처음으로 돌아간다.
				if("".equals(originalName)) continue;
				
				
				//파일명에서 확장자를 따낸다. (lastIndexOf를 가지고)
				String ext = originalName.substring(originalName.lastIndexOf("."));
				
				//UUID를 통해 생성된 문자열과 확장자를 결합해서 파일명을 완성한다.
				String saveFileName = getUuid() + ext;
				
				//물리적 경로에 새롭게 생성된 파일명으로 파일 저장
				mfile.transferTo(new File(path + File.separator + saveFileName)); //separator : OS별로 경로를 특수한 문자처리해준다.
				
				//파일처리 다했으면 Map에 저장해준다. (원래는 DB처리해야하는데 지금은 DB연결이 되어있지 않으므로)
				//폼값과 파일명을 저장할 Map컬렉션 생성
				Map<String, String> fileMap = new HashMap<String, String>();
				
				//원본파일명, 저장된파일명, 제목을 저장한다.
				fileMap.put("originalName", originalName);
				fileMap.put("saveFileName", saveFileName);
				fileMap.put("title", title);
				
				//하나의 파일정보를 저장한 Map을 List에 저장한다. (현재 파일이 2개이므로)
				resultList.add(fileMap);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("resultList",resultList);
		return "06FileUpload/uploadAction";
	}
}
