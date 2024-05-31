package viewer;

import connector.ConnectionMaker;
import controller.BoardController;
import controller.ReplyController;
import lombok.Setter;
import model.BoardDTO;
import model.ReplyDTO;
import model.UserDTO;
import util.ScannerUtil;

import java.util.ArrayList;
import java.util.Scanner;

@Setter
public class ReplyViewer {
    private UserDTO logIn;
    private BoardDTO curBoard;
    private final Scanner SCANNER;
    private ConnectionMaker connectionMaker;

    public ReplyViewer(ConnectionMaker connectionMaker, Scanner scanner, UserDTO logIn, BoardDTO curBoard) {
        this.connectionMaker = connectionMaker;
        this.SCANNER = scanner;
        this.logIn = logIn;
        this.curBoard = curBoard;
    }

    public void showListByBoardId() {
        ReplyController replyController = new ReplyController(connectionMaker);
        ArrayList<ReplyDTO> replyList = replyController.selectAllByBoardId(curBoard.getId());

        for (ReplyDTO reply : replyList) {
            System.out.printf("%d. %s : %s\n", reply.getId(), reply.getNickname(), reply.getContent());
        }

        String message = "댓글을 다시겠습니까? (Y/N)";
        String answer = ScannerUtil.nextLine(SCANNER, message);

        while (!(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n"))) {
            message = "Y or N을 입력해주세요";
            answer = ScannerUtil.nextLine(SCANNER, message);
        }

        if (answer.equalsIgnoreCase("y")) {
            leaveComment();
        } else {

        }
    }

    public void leaveComment() {
        ReplyController replyController = new ReplyController(connectionMaker);
        ReplyDTO reply = new ReplyDTO();

        reply.setBoardId(curBoard.getId());
        reply.setWriterId(logIn.getId());
        reply.setNickname(logIn.getNickname());

        String message = "댓글을 입력해주세요";
        reply.setContent(ScannerUtil.nextLine(SCANNER, message));

        replyController.insert(reply);
    }
}
