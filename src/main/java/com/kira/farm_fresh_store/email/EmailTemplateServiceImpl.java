package com.kira.farm_fresh_store.email;

import com.kira.farm_fresh_store.entity.order.Order;
import com.kira.farm_fresh_store.entity.order.OrderDetail;
import com.kira.farm_fresh_store.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService{
    private static final Locale VIETNAM_LOCALE = new Locale("vi", "VN");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(VIETNAM_LOCALE);
    @Override
    public String buildOrderConfirmationEmail(User user, Order order) {
        StringBuilder htmlBuilder = new StringBuilder();

        // Header
        htmlBuilder.append("<!DOCTYPE html>")
                .append("<html>")
                .append("<head>")
                .append("<meta charset=\"utf-8\">")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                .append("<title>Xác nhận đơn hàng</title>")
                .append("</head>")
                .append("<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; color: #333;\">")
                .append("<div style=\"max-width: 600px; margin: 0 auto; padding: 20px;\">");

        // Banner
        htmlBuilder.append("<div style=\"background-color: #4F46E5; padding: 20px; text-align: center; border-radius: 8px 8px 0 0; margin-bottom: 20px;\">")
                .append("<h1 style=\"color: white; margin: 0;\">Xác nhận đơn hàng</h1>")
                .append("</div>");

        // Greeting
        htmlBuilder.append("<div style=\"padding: 0 20px;\">")
                .append("<p style=\"font-size: 16px;\">Xin chào <strong>").append(user.getUsername()).append("</strong>,</p>")
                .append("<p style=\"font-size: 16px;\">Cảm ơn bạn đã đặt hàng tại cửa hàng chúng tôi! Đơn hàng của bạn đã được xác nhận và đang được xử lý.</p>");

        // Order details
        htmlBuilder.append("<div style=\"border: 1px solid #e0e0e0; border-radius: 8px; padding: 20px; margin-top: 20px; background-color: #f9f9f9;\">")
                .append("<h2 style=\"font-size: 18px; border-bottom: 1px solid #e0e0e0; padding-bottom: 10px; margin-top: 0;\">Thông tin đơn hàng</h2>")
                .append("<table style=\"width: 100%; border-collapse: collapse;\">")
                .append("<tr><td style=\"padding: 8px 0; font-weight: bold;\">Mã đơn hàng:</td><td style=\"padding: 8px 0;\">").append(order.getId()).append("</td></tr>")
                .append("<tr><td style=\"padding: 8px 0; font-weight: bold;\">Ngày đặt:</td><td style=\"padding: 8px 0;\">").append(order.getCreatedDate().format(DATE_FORMATTER)).append("</td></tr>")
                .append("<tr><td style=\"padding: 8px 0; font-weight: bold;\">Tổng tiền:</td><td style=\"padding: 8px 0; font-weight: bold; color: #4F46E5;\">").append(CURRENCY_FORMATTER.format(order.getTotalPrice())).append("</td></tr>")
                .append("</table>");

        // Order items if available
        if (order.getTotalItem() != null) {
            htmlBuilder.append("<h3 style=\"font-size: 16px; margin-top: 20px;\">Chi tiết sản phẩm</h3>")
                    .append("<table style=\"width: 100%; border-collapse: collapse;\">")
                    .append("<thead>")
                    .append("<tr style=\"background-color: #f0f0f0;\">")
                    .append("<th style=\"padding: 8px; text-align: left; border-bottom: 1px solid #ddd;\">Sản phẩm</th>")
                    .append("<th style=\"padding: 8px; text-align: center; border-bottom: 1px solid #ddd;\">Số lượng</th>")
                    .append("<th style=\"padding: 8px; text-align: right; border-bottom: 1px solid #ddd;\">Giá</th>")
                    .append("</tr>")
                    .append("</thead>")
                    .append("<tbody>");

            for (OrderDetail item : order.getOrderDetails()) {
                htmlBuilder.append("<tr>")
                        .append("<td style=\"padding: 8px; border-bottom: 1px solid #ddd;\">").append(item.getProduct().getName()).append("</td>")
                        .append("<td style=\"padding: 8px; text-align: center; border-bottom: 1px solid #ddd;\">").append(item.getQuantity()).append("</td>")
                        .append("<td style=\"padding: 8px; text-align: right; border-bottom: 1px solid #ddd;\">").append(CURRENCY_FORMATTER.format(item.getPrice())).append("</td>")
                        .append("</tr>");
            }

            htmlBuilder.append("</tbody></table>");
        }

        htmlBuilder.append("</div>"); // Close order details div

        // Footer message
        htmlBuilder.append("<div style=\"margin-top: 30px; font-size: 16px;\">")
                .append("<p>Chúng tôi sẽ sớm liên hệ để giao hàng. Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi.</p>")
                .append("<p>Trân trọng,<br><strong>Đội ngũ hỗ trợ</strong></p>")
                .append("</div>");

        // Company footer
        htmlBuilder.append("<div style=\"margin-top: 30px; padding: 20px; background-color: #f5f5f5; border-radius: 0 0 8px 8px; text-align: center; font-size: 14px; color: #666;\">")
                .append("<p>© 2025 Cửa hàng của chúng tôi. Tất cả các quyền được bảo lưu.</p>")
                .append("<p>Địa chỉ: 203 Phú thạnh, Huyện Cần Giuộc, Tỉnh Long An</p>")
                .append("</div>");

        // Close all tags
        htmlBuilder.append("</div>") // Close content div
                .append("</div>") // Close container div
                .append("</body>")
                .append("</html>");

        return htmlBuilder.toString();
    }
    @Override
    public String buildOrderCancellationEmail(User user, Order order) {
        StringBuilder htmlBuilder = new StringBuilder();

        // Header
        htmlBuilder.append("<!DOCTYPE html>")
                .append("<html>")
                .append("<head>")
                .append("<meta charset=\"utf-8\">")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                .append("<title>Thông báo hủy đơn hàng</title>")
                .append("</head>")
                .append("<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; color: #333;\">")
                .append("<div style=\"max-width: 600px; margin: 0 auto; padding: 20px;\">");

        // Banner
        htmlBuilder.append("<div style=\"background-color: #E53E3E; padding: 20px; text-align: center; border-radius: 8px 8px 0 0; margin-bottom: 20px;\">")
                .append("<h1 style=\"color: white; margin: 0;\">Đơn hàng đã bị hủy</h1>")
                .append("</div>");

        // Greeting
        htmlBuilder.append("<div style=\"padding: 0 20px;\">")
                .append("<p style=\"font-size: 16px;\">Xin chào <strong>").append(user.getUsername()).append("</strong>,</p>")
                .append("<p style=\"font-size: 16px; color: red;\"><strong>Đơn hàng của bạn đã bị hủy.</strong></p>")
                .append("<p style=\"font-size: 16px;\">Nếu bạn có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi.</p>");

        // Order details
        htmlBuilder.append("<div style=\"border: 1px solid #e0e0e0; border-radius: 8px; padding: 20px; margin-top: 20px; background-color: #f9f9f9;\">")
                .append("<h2 style=\"font-size: 18px; border-bottom: 1px solid #e0e0e0; padding-bottom: 10px; margin-top: 0;\">Thông tin đơn hàng</h2>")
                .append("<table style=\"width: 100%; border-collapse: collapse;\">")
                .append("<tr><td style=\"padding: 8px 0; font-weight: bold;\">Mã đơn hàng:</td><td style=\"padding: 8px 0;\">").append(order.getId()).append("</td></tr>")
                .append("<tr><td style=\"padding: 8px 0; font-weight: bold;\">Ngày đặt:</td><td style=\"padding: 8px 0;\">").append(order.getCreatedDate().format(DATE_FORMATTER)).append("</td></tr>")
                .append("<tr><td style=\"padding: 8px 0; font-weight: bold;\">Tổng tiền:</td><td style=\"padding: 8px 0; font-weight: bold; color: red;\">").append(CURRENCY_FORMATTER.format(order.getTotalPrice())).append("</td></tr>")
                .append("</table>")
                .append("</div>"); // Close order details div

        // Footer message
        htmlBuilder.append("<div style=\"margin-top: 30px; font-size: 16px;\">")
                .append("<p>Chúng tôi rất tiếc vì sự bất tiện này. Nếu bạn cần hỗ trợ, vui lòng liên hệ với bộ phận chăm sóc khách hàng.</p>")
                .append("<p>Trân trọng,<br><strong>Đội ngũ hỗ trợ</strong></p>")
                .append("</div>");

        // Company footer
        htmlBuilder.append("<div style=\"margin-top: 30px; padding: 20px; background-color: #f5f5f5; border-radius: 0 0 8px 8px; text-align: center; font-size: 14px; color: #666;\">")
                .append("<p>© 2025 Cửa hàng của chúng tôi. Tất cả các quyền được bảo lưu.</p>")
                .append("<p>Địa chỉ: 203 Phú Thạnh, Huyện Cần Giuộc, Tỉnh Long An</p>")
                .append("</div>");

        // Close all tags
        htmlBuilder.append("</div>") // Close content div
                .append("</div>") // Close container div
                .append("</body>")
                .append("</html>");

        return htmlBuilder.toString();
    }

}
