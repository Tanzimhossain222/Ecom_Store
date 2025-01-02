package com.ecom.shoping_cart.service.impl;

import com.ecom.shoping_cart.service.CommonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class CommonServiceImpl implements CommonService {
    @Override
    public void removeSessionMessage() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = (HttpServletRequest) attributes.getRequest();
            HttpSession session = request.getSession();
            session.removeAttribute("successMsg");
            session.removeAttribute("errorMsg");
        }
    }
}