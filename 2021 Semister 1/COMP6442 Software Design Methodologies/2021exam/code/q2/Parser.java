import java.util.LinkedList;
import java.util.List;

public class Parser {

	private final Tokeniser tokeniser;

	public Parser(Tokeniser tokeniser) {
		this.tokeniser = tokeniser;
	}

	public List<Command> parseCmds() {

		List<Command> commands = new LinkedList<>();

		// TODO
		// ########## YOUR CODE STARTS HERE ##########
		Token token = tokeniser.current();
		boolean flag = true;

		while (tokeniser.hasNext() && flag) {
			Command cmd;
			System.out.println(tokeniser.current());
			if (Token.Type.RETRIEVE == token.getType()) {
				System.out.println("> Type RETRIEVE.");

				RetrieveCommand c = new RetrieveCommand();
				tokeniser.next();
				token = tokeniser.current();
				System.out.println(tokeniser.current());
				if (Token.Type.PARAMETER == token.getType()) {
					System.out.println("> Type PARAMETER.");
					c.setKey(token.getValue());
					tokeniser.next();

				} else {
					flag = false;
				}

				token = tokeniser.current();
				if (Token.Type.FROM == token.getType()) {
					System.out.println("> Type FROM.");

					tokeniser.next();
				} else {
					flag = false;
				}

				token = tokeniser.current();
				if (Token.Type.PARAMETER == token.getType()) {
					System.out.println("> Type PARAMETER.");
					c.setFileName(token.getValue());
					tokeniser.next();
				} else {
					flag = false;
				}

				if (!flag) {
					System.out.println("!!!!!");
				}

				cmd = c;
				commands.add(cmd);
				System.out.println("Done RETRIEVE.");
				token = tokeniser.current();
			}
			else if (Token.Type.STORE == token.getType()) {
				System.out.println("> Type STORE.");

				StoreCommand c = new StoreCommand();
				tokeniser.next();
				token = tokeniser.current();
				if (Token.Type.PARAMETER == token.getType()) {
					System.out.println("> Type PARAMETER.");
					c.setKey(token.getValue());
					tokeniser.next();

				} else {
					flag = false;
				}

				token = tokeniser.current();
				if (Token.Type.TO == token.getType()) {
					System.out.println("> Type To.");

					tokeniser.next();
				} else {
					flag = false;
				}

				token = tokeniser.current();
				if (Token.Type.PARAMETER == token.getType()) {
					System.out.println("> Type PARAMETER.");

					c.setFileName(token.getValue());
					tokeniser.next();

				} else {
					flag = false;
				}

				if (!flag) {
					System.out.println("!!!!!");
				}

				cmd = c;
				commands.add(cmd);
				System.out.println("Done STORE.");
				token = tokeniser.current();
			}
			else if (Token.Type.TERMINATOR == token.getType()) {
				System.out.println("> Type TERMINATOR.");
				tokeniser.next();
				token = tokeniser.current();
				System.out.println("Done TERMINATOR.");
			}
			else {
				System.out.println("> Type else:"+token.getType());
				flag = false;
			}

		}
		// ########## YOUR CODE ENDS HERE ##########

		return commands;
	}
}
