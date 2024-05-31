package viewer;

import connector.ConnectionMaker;
import connector.MySqlConnectionMaker;
import controller.UserController;
import model.UserDTO;
import util.ScannerUtil;

import java.util.Scanner;

public class UserViewer {
    private final Scanner SCANNER = new Scanner(System.in);
    private UserDTO logIn;
    private ConnectionMaker connectionMaker;

    public UserViewer() {
        connectionMaker = new MySqlConnectionMaker();
    }

    public void showIndex() {
        String message = "1. 로그인 2. 회원가입 3. 종료";

        while (true) {
            int choice = ScannerUtil.nextInt(SCANNER, message, 1, 3);
            if (choice == 1) {
                auth();
                if (logIn != null) {
                    showMenu();
                }
            } else if (choice == 2) {
                register();
            } else if (choice == 3) {
                System.out.println("프로그램을 종료합니다");
                break;
            }
        }
    }

    public void auth() {
        String message = "아이디를 입력하세요";
        String username = ScannerUtil.nextLine(SCANNER, message);
        message = "비밀번호를 입력해주세요";
        String password = ScannerUtil.nextLine(SCANNER, message);

        UserController userController = new UserController(connectionMaker);
        UserDTO user = userController.auth(username, password);

        if (user == null) {
            System.out.println("잘못된 정보입니다");
        } else {
            logIn = user;
        }
    }

    public void register() {
        UserController userController = new UserController(connectionMaker);

        String message = "사용하실 아이디를 입력하세요";
        String username = ScannerUtil.nextLine(SCANNER, message);

        message = "사용하실 비밀번호를 입력하세요";
        String password = ScannerUtil.nextLine(SCANNER, message);

        message = "사용하실 닉네임를 입력하세요";
        String nickname = ScannerUtil.nextLine(SCANNER, message);

        UserDTO attempt = new UserDTO();
        attempt.setUsername(username);
        attempt.setPassword(password);
        attempt.setNickname(nickname);

        if (!userController.register(attempt)) {
            System.out.println("중복된 아이디거나 닉네임입니다");
        } else {
            System.out.println("아이디 생성에 성공하셨습니다");
        }
    }

    public void showMenu() {
        String message = "1. 게시판으로 2. 회원 정보 확인 3. 로그아웃";

        while(logIn != null) {
            int choice = ScannerUtil.nextInt(SCANNER, message, 1, 3);

            if (choice == 1) {
                BoardViewer boardViewer = new BoardViewer(connectionMaker, SCANNER, logIn);
                boardViewer.showMenu();
            } else if (choice == 2) {
                showUserInfo();
            } else if (choice == 3) {
                System.out.println("로그아웃합니다");
                logIn = null;
            }
        }
    }

    public void showUserInfo() {
        System.out.printf("%d. %s 닉네임: %s\n", logIn.getId(), logIn.getUsername(), logIn.getNickname());

        String message = "1. 정보 수정 2. 회원 탈퇴 3. 뒤로 가기";
        int choice = ScannerUtil.nextInt(SCANNER, message, 1, 3);

        if (choice == 1) {
            update();
            showUserInfo();
        } else if (choice == 2) {
            delete();
        } else if (choice == 3) {

        }
    }

    public void update() {
        String message = "바꿀 비밀번호를 입력하세요";
        logIn.setPassword(ScannerUtil.nextLine(SCANNER, message));

        message = "바꿀 닉네임을 입력하세요";
        logIn.setNickname(ScannerUtil.nextLine(SCANNER, message));

        UserController userController = new UserController(connectionMaker);
        userController.update(logIn);
    }

    public void delete() {
        UserController userController = new UserController(connectionMaker);
        userController.delete(logIn.getId());
        logIn = null;
    }


















}
