package viewer;

import connector.ConnectionMaker;
import controller.BoardController;
import model.BoardDTO;
import model.UserDTO;
import util.ScannerUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class BoardViewer {
    private ConnectionMaker connectionMaker;
    private final Scanner SCANNER;
    private UserDTO logIn;
    private BoardDTO curBoard;

    public BoardViewer(ConnectionMaker connectionMaker, Scanner scanner, UserDTO logIn) {
        this.connectionMaker = connectionMaker;
        SCANNER = scanner;
        this.logIn = logIn;
    }

    public void showMenu() {
        String message = "1. 글 목록보기 2. 글 작성하기 3. 뒤로가기";

        while (true) {
            int choice = ScannerUtil.nextInt(SCANNER, message, 1, 3);
            if (choice == 1) {
                showTitle();
            } else if (choice == 2) {
                register();
            } else if (choice == 3) {
                System.out.println("뒤로 값니다");
                break;
            }
        }
    }

    public void showTitle() {
        BoardController boardController = new BoardController(connectionMaker);
        ArrayList<BoardDTO> boardList = boardController.selectAll();

        for (BoardDTO board : boardList) {
            System.out.printf("%d. %s\n", board.getId(), board.getTitle());
        }

        String message = "상세히 볼 글을 입력하세요(뒤로가기 0)";
        int choice = ScannerUtil.nextInt(SCANNER, message);

        while (boardController.selectOne(choice) == null) {
            if (choice == 0) return;
            message = "제대로 입력해주세요";
            choice = ScannerUtil.nextInt(SCANNER, message);
        }

        showOne(choice);
    }

    public void register() {
        BoardDTO board = new BoardDTO();

        String message = "제목을 입력하세요";
        board.setTitle(ScannerUtil.nextLine(SCANNER, message));

        message = "내용을 입력하세요";
        board.setTitle(ScannerUtil.nextLine(SCANNER, message));

        board.setWriterId(logIn.getId());

        BoardController boardController = new BoardController(connectionMaker);

        boardController.insert(board);
    }

    public void showOne(int id) {
        BoardController boardController = new BoardController(connectionMaker);
        curBoard = boardController.selectOne(id);

        System.out.printf("%d. %s - %s\n", curBoard.getId(), curBoard.getTitle(), curBoard.getNickname());
        System.out.printf("%s", curBoard.getContent());

        String message = "1. 수정 2. 삭제 3. 댓글 목록 4. 뒤로가기";

        int choice = ScannerUtil.nextInt(SCANNER, message, 1, 4);

        if (choice == 1) {
            if (curBoard.getWriterId() == logIn.getId()) {
                update(id);
            } else {
                System.out.println("권한이 없습니다");
            }
            showOne(id);
        } else if (choice == 2) {
            if (curBoard.getWriterId() == logIn.getId()) {
                delete(id);
            } else {
                System.out.println("권한이 없습니다");
                showOne(id);
            }
        } else if (choice == 3) {
            //replyViewer
            ReplyViewer replyViewer = new ReplyViewer(connectionMaker, SCANNER, logIn, curBoard);
        } else if (choice == 4) {
            showTitle();
        }
    }

    public void update(int id) {
        BoardController boardController = new BoardController(connectionMaker);
        BoardDTO board = boardController.selectOne(id);

        String message = "제목을 입력하세요";
        board.setTitle(ScannerUtil.nextLine(SCANNER, message));
        message = "내용을 입력하세요";
        board.setContent(ScannerUtil.nextLine(SCANNER, message));

        boardController.update(board);
    }

    public void delete(int id) {
        String message = "정말로 지우시겠습니까? (Y)";
        String answer = ScannerUtil.nextLine(SCANNER, message);

        if (answer.equalsIgnoreCase("Y")) {
            BoardController boardController = new BoardController(connectionMaker);
            boardController.delete(id);
            showTitle();
        } else {
            showOne(id);
        }
    }

}
