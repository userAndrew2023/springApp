package com.socialstock;

import com.socialstock.logic.Trading;
import com.socialstock.payment.PaymentLogic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.IOException;

import static com.socialstock.logic.Trading.stocks;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class Application {

	public static void main(String[] args) throws IOException {
		stocks.put("IHC", new Trading("Invest House Channel", "IHC", 0.5f));
		stocks.put("PEL", new Trading("Pelmeny House", "PEL", 1.57f));
		stocks.put("VRL", new Trading("Varlamov Channel", "VRL", 478.4f));
		stocks.put("TLL", new Trading("Topor Live Channel", "TLL", 0.5f));
		stocks.put("TLE", new Trading("Topor 18+ Channel", "TLE", 0.5f));
		stocks.put("TLP", new Trading("Topor Politics Channel", "TLP", 0.5f));
		stocks.put("IGRM", new Trading("Ideogram IT", "IGRM", 1017.58f));

		for (var i : stocks.entrySet()) {
			i.getValue().run();
		}
		System.out.println(stocks.get("IHC").getPrice());
        SpringApplication.run(Application.class, args);
	}
}
