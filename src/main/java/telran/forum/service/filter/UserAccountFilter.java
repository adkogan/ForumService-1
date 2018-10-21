package telran.forum.service.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import telran.forum.configuration.AccountConfiguration;
import telran.forum.configuration.AccountUserCredential;
import telran.forum.dao.UserAccountRepository;
import telran.forum.domain.UserAccount;

@Service
@Order(2)
public class UserAccountFilter implements Filter {

	@Autowired
	AccountConfiguration userConfiguration;

	@Autowired
	UserAccountRepository accountRepository;

	@Override
	public void doFilter(ServletRequest reqs, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		// TODO UserAccountFilter method doFilter
		HttpServletRequest request = (HttpServletRequest) reqs;
		HttpServletResponse response = (HttpServletResponse) resp;
		if (request.getServletPath().startsWith("/account")) {
			if ("put".equalsIgnoreCase(request.getMethod()) ||
					"delete".equalsIgnoreCase(request.getMethod())) {
				String auth = request.getHeader("Authorization");
				AccountUserCredential userCredential = userConfiguration.tokenDecode(auth);
				UserAccount userAccount = accountRepository.findById(userCredential.getLogin()).orElse(null);
				if (userAccount == null) {
					response.sendError(401, "Unauthorized");
				} else {
					if (!userAccount.getPassword().equals(userCredential.getPassword())) {
						response.sendError(403, "Forbidden");
					}
				}

			}

		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
