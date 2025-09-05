package org.jetblue.jetblue.Utils;

import com.stripe.model.PaymentIntent;
import org.jetblue.jetblue.Models.DAO.BookingPassengerPayment;
import org.jetblue.jetblue.Models.DAO.User;

import java.math.BigDecimal;
import java.util.List;

public class MailTemplatesUtils {

    public static String generateReceiptEmailBody(String passengerName, String flightNumber, String departure, String arrival, String seatClass, String bookingReference) {
        return String.format("""
                Dear %s,
                
                Thank you for choosing JetBlue for your travel needs. We are pleased to confirm your booking with the following details:
                
                Flight Number: %s
                Departure: %s
                Arrival: %s
                Seat Class: %s
                Booking Reference: %s
                
                We look forward to welcoming you on board. If you have any questions or need further assistance, please do not hesitate to contact us.
                
                Safe travels!
                
                Best regards,
                The JetBlue Team
                """, passengerName, flightNumber, departure, arrival, seatClass, bookingReference);
    }

    public static String generateCancellationEmailBody(String passengerName, String flightNumber, String departure, String arrival, String seatClass, String bookingReference) {
        return String.format("""
                Dear %s,
                
                We regret to inform you that your booking with JetBlue has been cancelled. Below are the details of your cancelled booking:
                
                Flight Number: %s
                Departure: %s
                Arrival: %s
                Seat Class: %s
                Booking Reference: %s
                
                We apologize for any inconvenience this may cause. If you have any questions or need further assistance, please do not hesitate to contact us.
                
                Thank you for your understanding.
                
                Best regards,
                The JetBlue Team
                """, passengerName, flightNumber, departure, arrival, seatClass, bookingReference);
    }

    public static String generateWelcomeEmailBody(String passengerName) {
        return generateTemplateHtmlWelcome().replace("{{full_name}}", passengerName);
    }

    public static String generateReceiptEmail(User user, List<BookingPassengerPayment> passengerPayments, PaymentIntent paymentIntent) {
        StringBuilder receiptDetails = new StringBuilder();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (BookingPassengerPayment p : passengerPayments) {
            String seatLabel = p.getPayment().getBooking().getSeat().getSeatLabel();
            String amount = p.getPayment().getAmount();
            totalAmount = totalAmount.add(new BigDecimal(amount));
            String passengerName = p.getPassenger().getFirstName() + " " + p.getPassenger().getLastName();
            receiptDetails.append("""
                    <tr>
                        <td width="50%%" class="purchase_item"><span class="f-fallback">%s</span></td>
                        <td width="30%%" class="purchase_item"><span class="f-fallback">%s</span></td>
                        <td width="20%%" class="align-right purchase_item"><span class="f-fallback">$%s</span></td>
                    </tr>
                    """.formatted(passengerName, seatLabel, amount));


        }

        String html = generateTemplateHtmlEmailReceipt()
                .replace("{{name}}", user.getUsername())
                .replace("{{receipt_id}}", paymentIntent.getId())
                .replace("{{date}}", java.time.LocalDate.now().toString())
                .replace("{{total}}", "$" + totalAmount.toPlainString())
                .replace("{{receipt_details}}", receiptDetails.toString())
                .replace("{{credit_card_brand}}", "VISA") // optional, from PaymentIntent metadata
                .replace("{{credit_card_last_four}}", "4242") // optional, from PaymentIntent metadata
                .replace("{{billing_url}}", "https://example.com/billing")
                .replace("{{support_url}}", "https://example.com/support")
                .replace("{{action_url}}", "https://example.com/download/receipt")
                .replace("{{purchase_date}}", java.time.LocalDate.now().toString());

        return html;
    }

    public static String generateVerificationEmail(String username, String verificationLink) {
        return generateTemplateHtmlEmailVerification()
                .replace("{{user_name}}", username)
                .replace("{{current_year}}", String.valueOf(java.time.Year.now().getValue()))
                .replace("{{verification_url}}", verificationLink);
    }





    public static String  generateTemplateHtmlWelcome(){
        return
                """
                        <!DOCTYPE html>
                        <html lang="en">
                        <head>
                            <meta charset="UTF-8">
                            <title>Welcome to JetBlue</title>
                        </head>
                        <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0;">
                            <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%">
                                <tr>
                                    <td align="center" style="padding: 20px 0;">
                                        <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="600" style="background: #ffffff; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
                                            <tr>
                                                <td style="padding: 30px; color: #333333;">
                                                    <h2 style="color: #0047ab; margin-top: 0;">Dear {{full_name}},</h2>
                                                   \s
                                                    <p>Welcome to <strong>JetBlue</strong>! We are thrilled to have you as a part of our travel family. Whether you're flying for business or leisure, we are committed to providing you with an exceptional experience.</p>
                                                   \s
                                                    <p>As a valued customer, you can look forward to:</p>
                                                    <ul style="padding-left: 20px; color: #444444;">
                                                        <li>Comfortable seating and spacious cabins</li>
                                                        <li>Delicious in-flight meals and snacks</li>
                                                        <li>Friendly and attentive service from our crew</li>
                                                        <li>A wide range of entertainment options</li>
                                                    </ul>
                                                   \s
                                                    <p>We are dedicated to making your journey with us enjoyable and memorable. If you have any questions or need assistance, please do not hesitate to reach out to our customer service team.</p>
                                                   \s
                                                    <p>Thank you for choosing <strong>JetBlue</strong>. We look forward to serving you on your next flight!</p>
                                                   \s
                                                    <p style="margin-top: 30px;">Best regards,<br>
                                                    <strong>The JetBlue Team</strong></p>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </body>
                        </html>
                        
                """;
    }
    public static String generateTemplateHtmlEmailReceipt() {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <title>Payment Receipt</title>
                    <style>
                        body { font-family: Arial, sans-serif; color: #333; background-color: #F2F4F6; }
                        .container { max-width: 600px; margin: auto; padding: 20px; background-color: #fff; border-radius: 10px; }
                        h1 { color: #333; font-size: 22px; }
                        h3 { font-size: 14px; color: #333; }
                        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
                        th, td { padding: 12px; border-bottom: 1px solid #ddd; text-align: left; }
                        th { background-color: #f2f2f2; }
                        .total { font-weight: bold; text-align: right; }
                        .total_label{text-align:left; width:90%}
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>Hi {{name}},</h1>
                        <p>Thank you for your payment. This is your receipt for the recent booking.</p>
                
                        <table>
                            <thead>
                                <tr>
                                    <th>Passenger Name</th>
                                    <th>Seat Number</th>
                                    <th>Amount (USD)</th>
                                </tr>
                            </thead>
                            <tbody>
                                {{receipt_details}}
                                <tr>
                                    <td class="total total_label">Total</td>
                                     <td></td>
                                     <td class="total total_price">{{total}}</td>
                                </tr>
                            </tbody>
                        </table>
                
                        <p><strong>Transaction ID:</strong> {{receipt_id}}</p>
                        <p>If you have any questions, contact our <a href="{{support_url}}">support team</a>.</p>
                        <p>Cheers,<br>[Product Name] Team</p>
                    </div>
                </body>
                </html>
                """;
    }

    public static String generateTemplateHtmlEmailVerification() {
        return """
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
                <html xmlns="http://www.w3.org/1999/xhtml">
                  <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <meta name="x-apple-disable-message-reformatting" />
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                    <meta name="color-scheme" content="light dark" />
                    <meta name="supported-color-schemes" content="light dark" />
                    <title>Verify Your Email Address</title>
                    <style type="text/css" rel="stylesheet" media="all">
                    /* Base ------------------------------ */
                   \s
                    @import url("https://fonts.googleapis.com/css?family=Nunito+Sans:400,700&display=swap");
                    body {
                      width: 100% !important;
                      height: 100%;
                      margin: 0;
                      -webkit-text-size-adjust: none;
                    }
                   \s
                    a {
                      color: #3869D4;
                    }
                   \s
                    a img {
                      border: none;
                    }
                   \s
                    td {
                      word-break: break-word;
                    }
                   \s
                    .preheader {
                      display: none !important;
                      visibility: hidden;
                      mso-hide: all;
                      font-size: 1px;
                      line-height: 1px;
                      max-height: 0;
                      max-width: 0;
                      opacity: 0;
                      overflow: hidden;
                    }
                    /* Type ------------------------------ */
                   \s
                    body,
                    td,
                    th {
                      font-family: "Nunito Sans", Helvetica, Arial, sans-serif;
                    }
                   \s
                    h1 {
                      margin-top: 0;
                      color: #333333;
                      font-size: 22px;
                      font-weight: bold;
                      text-align: left;
                    }
                   \s
                    h2 {
                      margin-top: 0;
                      color: #333333;
                      font-size: 16px;
                      font-weight: bold;
                      text-align: left;
                    }
                   \s
                    h3 {
                      margin-top: 0;
                      color: #333333;
                      font-size: 14px;
                      font-weight: bold;
                      text-align: left;
                    }
                   \s
                    td,
                    th {
                      font-size: 16px;
                    }
                   \s
                    p,
                    ul,
                    ol,
                    blockquote {
                      margin: .4em 0 1.1875em;
                      font-size: 16px;
                      line-height: 1.625;
                    }
                   \s
                    p.sub {
                      font-size: 13px;
                    }
                    /* Utilities ------------------------------ */
                   \s
                    .align-right {
                      text-align: right;
                    }
                   \s
                    .align-left {
                      text-align: left;
                    }
                   \s
                    .align-center {
                      text-align: center;
                    }
                   \s
                    .u-margin-bottom-none {
                      margin-bottom: 0;
                    }
                    /* Buttons ------------------------------ */
                   \s
                    .button {
                      background-color: #3869D4;
                      border-top: 10px solid #3869D4;
                      border-right: 18px solid #3869D4;
                      border-bottom: 10px solid #3869D4;
                      border-left: 18px solid #3869D4;
                      display: inline-block;
                      color: #FFF;
                      text-decoration: none;
                      border-radius: 3px;
                      box-shadow: 0 2px 3px rgba(0, 0, 0, 0.16);
                      -webkit-text-size-adjust: none;
                      box-sizing: border-box;
                    }
                   \s
                    .button--green {
                      background-color: #22BC66;
                      border-top: 10px solid #22BC66;
                      border-right: 18px solid #22BC66;
                      border-bottom: 10px solid #22BC66;
                      border-left: 18px solid #22BC66;
                    }
                   \s
                    .button--red {
                      background-color: #FF6136;
                      border-top: 10px solid #FF6136;
                      border-right: 18px solid #FF6136;
                      border-bottom: 10px solid #FF6136;
                      border-left: 18px solid #FF6136;
                    }
                   \s
                    .button--orange {
                      background-color: #FF8C42;
                      border-top: 10px solid #FF8C42;
                      border-right: 18px solid #FF8C42;
                      border-bottom: 10px solid #FF8C42;
                      border-left: 18px solid #FF8C42;
                    }
                   \s
                    @media only screen and (max-width: 500px) {
                      .button {
                        width: 100% !important;
                        text-align: center !important;
                      }
                    }
                   \s
                    /* Verification Code Box ------------------------------ */
                   \s
                    .verification-code {
                      width: 100%;
                      margin: 30px 0;
                      padding: 24px;
                      -premailer-width: 100%;
                      -premailer-cellpadding: 0;
                      -premailer-cellspacing: 0;
                      background-color: #F4F4F7;
                      border: 2px solid #3869D4;
                      border-radius: 8px;
                      text-align: center;
                    }
                   \s
                    .verification-code h2 {
                      margin: 0 0 10px 0;
                      color: #3869D4;
                      font-size: 24px;
                      font-weight: bold;
                      letter-spacing: 4px;
                      font-family: monospace;
                    }
                   \s
                    .verification-code p {
                      margin: 0;
                      color: #51545E;
                      font-size: 14px;
                    }
                   \s
                    /* Expiration Warning ------------------------------ */
                   \s
                    .expiration-warning {
                      width: 100%;
                      margin: 20px 0;
                      padding: 16px;
                      background-color: #FFF5F5;
                      border: 1px solid #FED7D7;
                      border-radius: 6px;
                      text-align: center;
                    }
                   \s
                    .expiration-warning p {
                      margin: 0;
                      color: #C53030;
                      font-size: 14px;
                      font-weight: bold;
                    }
                   \s
                    /* Icon Styles ------------------------------ */
                   \s
                    .icon-checkmark {
                      width: 60px;
                      height: 60px;
                      margin: 0 auto 20px auto;
                      background-color: #22BC66;
                      border-radius: 50%;
                      display: flex;
                      align-items: center;
                      justify-content: center;
                    }
                   \s
                    .icon-checkmark::before {
                      content: "✓";
                      color: white;
                      font-size: 30px;
                      font-weight: bold;
                    }
                   \s
                    body {
                      background-color: #F2F4F6;
                      color: #51545E;
                    }
                   \s
                    p {
                      color: #51545E;
                    }
                   \s
                    .email-wrapper {
                      width: 100%;
                      margin: 0;
                      padding: 0;
                      -premailer-width: 100%;
                      -premailer-cellpadding: 0;
                      -premailer-cellspacing: 0;
                      background-color: #F2F4F6;
                    }
                   \s
                    .email-content {
                      width: 100%;
                      margin: 0;
                      padding: 0;
                      -premailer-width: 100%;
                      -premailer-cellpadding: 0;
                      -premailer-cellspacing: 0;
                    }
                    /* Masthead ----------------------- */
                   \s
                    .email-masthead {
                      padding: 25px 0;
                      text-align: center;
                    }
                   \s
                    .email-masthead_logo {
                      width: 94px;
                    }
                   \s
                    .email-masthead_name {
                      font-size: 20px;
                      font-weight: bold;
                      color: #3869D4;
                      text-decoration: none;
                      text-shadow: 0 1px 0 white;
                    }
                    /* Body ------------------------------ */
                   \s
                    .email-body {
                      width: 100%;
                      margin: 0;
                      padding: 0;
                      -premailer-width: 100%;
                      -premailer-cellpadding: 0;
                      -premailer-cellspacing: 0;
                    }
                   \s
                    .email-body_inner {
                      width: 570px;
                      margin: 0 auto;
                      padding: 0;
                      -premailer-width: 570px;
                      -premailer-cellpadding: 0;
                      -premailer-cellspacing: 0;
                      background-color: #FFFFFF;
                    }
                   \s
                    .email-footer {
                      width: 570px;
                      margin: 0 auto;
                      padding: 0;
                      -premailer-width: 570px;
                      -premailer-cellpadding: 0;
                      -premailer-cellspacing: 0;
                      text-align: center;
                    }
                   \s
                    .email-footer p {
                      color: #A8AAAF;
                    }
                   \s
                    .body-action {
                      width: 100%;
                      margin: 30px auto;
                      padding: 0;
                      -premailer-width: 100%;
                      -premailer-cellpadding: 0;
                      -premailer-cellspacing: 0;
                      text-align: center;
                    }
                   \s
                    .body-sub {
                      margin-top: 25px;
                      padding-top: 25px;
                      border-top: 1px solid #EAEAEC;
                    }
                   \s
                    .content-cell {
                      padding: 45px;
                    }
                    /*Media Queries ------------------------------ */
                   \s
                    @media only screen and (max-width: 600px) {
                      .email-body_inner,
                      .email-footer {
                        width: 100% !important;
                      }
                    }
                   \s
                    @media (prefers-color-scheme: dark) {
                      body,
                      .email-body,
                      .email-body_inner,
                      .email-content,
                      .email-wrapper,
                      .email-masthead,
                      .email-footer {
                        background-color: #333333 !important;
                        color: #FFF !important;
                      }
                      p,
                      ul,
                      ol,
                      blockquote,
                      h1,
                      h2,
                      h3,
                      span {
                        color: #FFF !important;
                      }
                      .verification-code,
                      .expiration-warning {
                        background-color: #222 !important;
                      }
                      .email-masthead_name {
                        text-shadow: none !important;
                      }
                    }
                   \s
                    :root {
                      color-scheme: light dark;
                      supported-color-schemes: light dark;
                    }
                    </style>
                    <!--[if mso]>
                    <style type="text/css">
                      .f-fallback  {
                        font-family: Arial, sans-serif;
                      }
                    </style>
                  <![endif]-->
                  </head>
                  <body>
                    <span class="preheader">Please verify your email address to complete your registration with JetBlue Flight Service.</span>
                    <table class="email-wrapper" width="100%" cellpadding="0" cellspacing="0" role="presentation">
                      <tr>
                        <td align="center">
                          <table class="email-content" width="100%" cellpadding="0" cellspacing="0" role="presentation">
                            <tr>
                              <td class="email-masthead">
                                <a href="{{company_url}}" class="f-fallback email-masthead_name">
                                ✈️ JetBlue Flight Service
                              </a>
                              </td>
                            </tr>
                            <!-- Email Body -->
                            <tr>
                              <td class="email-body" width="570" cellpadding="0" cellspacing="0">
                                <table class="email-body_inner" align="center" width="570" cellpadding="0" cellspacing="0" role="presentation">
                                  <!-- Body content -->
                                  <tr>
                                    <td class="content-cell">
                                      <div class="f-fallback">
                                        <h1>Welcome {{user_name}}! 🎉</h1>
                                        <p>Thanks for signing up for <strong>JetBlue Flight Service</strong>! We're excited to have you on board.</p>
                                        <p>To complete your registration and start booking flights, please verify your email address by clicking the button below:</p>
                                       \s
                                        <!-- Action Button -->
                                        <table class="body-action" align="center" width="100%" cellpadding="0" cellspacing="0" role="presentation">
                                          <tr>
                                            <td align="center">
                                              <table width="100%" border="0" cellspacing="0" cellpadding="0" role="presentation">
                                                <tr>
                                                  <td align="center">
                                                    <a href="{{verification_url}}" class="f-fallback button button--green" target="_blank">Verify Email Address</a>
                                                  </td>
                                                </tr>
                                              </table>
                                            </td>
                                          </tr>
                                        </table>
                                       \s
                                        <!-- Alternative Verification Method -->
                                       \s
                                        <h3>What happens after verification?</h3>
                                        <ul>
                                          <li><strong>Search & Book Flights:</strong> Access our comprehensive flight search engine</li>
                                          <li><strong>Manage Bookings:</strong> View, modify, and cancel your reservations</li>
                                          <li><strong>Exclusive Offers:</strong> Receive special deals and promotions</li>
                                          <li><strong>24/7 Support:</strong> Get help whenever you need it</li>
                                        </ul>
                                       \s
                                        <p>If you didn't create an account with us, please ignore this email or <a href="{{support_url}}">contact our support team</a>.</p>
                                       \s
                                        <p>Safe travels!<br>
                                          <strong>The JetBlue Flight Service Team</strong></p>
                                       \s
                                        <!-- Sub copy -->
                                        <table class="body-sub" role="presentation">
                                          <tr>
                                            <td>
                                              <p class="f-fallback sub">
                                                <strong>Trouble clicking the button?</strong>\s
                                                Copy and paste this URL into your web browser:
                                              </p>
                                              <p class="f-fallback sub">
                                                <a href="{{verification_url}}">Click to verify your mail</a>
                                              </p>
                                            </td>
                                          </tr>
                                        </table>
                                      </div>
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                            <tr>
                              <td>
                                <table class="email-footer" align="center" width="570" cellpadding="0" cellspacing="0" role="presentation">
                                  <tr>
                                    <td class="content-cell" align="center">
                                      <p class="f-fallback sub align-center">
                                        JetBlue Flight Service<br>
                                        📧 <a href="mailto:support@jetblue-service.com">support@jetblue-service.com</a><br>
                                        🌐 <a href="{{company_url}}">www.jetblue-service.com</a>
                                      </p>
                                      <p class="f-fallback sub align-center">
                                        © {{current_year}} JetBlue Flight Service. All rights reserved.
                                      </p>
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </body>
                </html>
                """;
    }

    public static String generateTemplateHtmlEmailVerificationWithCode() {
        return """
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
                <html xmlns="http://www.w3.org/1999/xhtml">
                  <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <meta name="x-apple-disable-message-reformatting" />
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                    <meta name="color-scheme" content="light dark" />
                    <meta name="supported-color-schemes" content="light dark" />
                    <title>Verify Your Email Address</title>
                    <style type="text/css" rel="stylesheet" media="all">
                    /* Base ------------------------------ */
                   \s
                    @import url("https://fonts.googleapis.com/css?family=Nunito+Sans:400,700&display=swap");
                    body {
                      width: 100% !important;
                      height: 100%;
                      margin: 0;
                      -webkit-text-size-adjust: none;
                    }
                   \s
                    a {
                      color: #3869D4;
                    }
                   \s
                    a img {
                      border: none;
                    }
                   \s
                    td {
                      word-break: break-word;
                    }
                   \s
                    .preheader {
                      display: none !important;
                      visibility: hidden;
                      mso-hide: all;
                      font-size: 1px;
                      line-height: 1px;
                      max-height: 0;
                      max-width: 0;
                      opacity: 0;
                      overflow: hidden;
                    }
                    /* Type ------------------------------ */
                   \s
                    body,
                    td,
                    th {
                      font-family: "Nunito Sans", Helvetica, Arial, sans-serif;
                    }
                   \s
                    h1 {
                      margin-top: 0;
                      color: #333333;
                      font-size: 22px;
                      font-weight: bold;
                      text-align: left;
                    }
                   \s
                    h2 {
                      margin-top: 0;
                      color: #333333;
                      font-size: 16px;
                      font-weight: bold;
                      text-align: left;
                    }
                   \s
                    h3 {
                      margin-top: 0;
                      color: #333333;
                      font-size: 14px;
                      font-weight: bold;
                      text-align: left;
                    }
                   \s
                    td,
                    th {
                      font-size: 16px;
                    }
                   \s
                    p,
                    ul,
                    ol,
                    blockquote {
                      margin: .4em 0 1.1875em;
                      font-size: 16px;
                      line-height: 1.625;
                    }
                   \s
                    p.sub {
                      font-size: 13px;
                    }
                    /* Utilities ------------------------------ */
                   \s
                    .align-right {
                      text-align: right;
                    }
                   \s
                    .align-left {
                      text-align: left;
                    }
                   \s
                    .align-center {
                      text-align: center;
                    }
                   \s
                    .u-margin-bottom-none {
                      margin-bottom: 0;
                    }
                    /* Buttons ------------------------------ */
                   \s
                    .button {
                      background-color: #3869D4;
                      border-top: 10px solid #3869D4;
                      border-right: 18px solid #3869D4;
                      border-bottom: 10px solid #3869D4;
                      border-left: 18px solid #3869D4;
                      display: inline-block;
                      color: #FFF;
                      text-decoration: none;
                      border-radius: 3px;
                      box-shadow: 0 2px 3px rgba(0, 0, 0, 0.16);
                      -webkit-text-size-adjust: none;
                      box-sizing: border-box;
                    }
                   \s
                    .button--green {
                      background-color: #22BC66;
                      border-top: 10px solid #22BC66;
                      border-right: 18px solid #22BC66;
                      border-bottom: 10px solid #22BC66;
                      border-left: 18px solid #22BC66;
                    }
                   \s
                    .button--red {
                      background-color: #FF6136;
                      border-top: 10px solid #FF6136;
                      border-right: 18px solid #FF6136;
                      border-bottom: 10px solid #FF6136;
                      border-left: 18px solid #FF6136;
                    }
                   \s
                    .button--orange {
                      background-color: #FF8C42;
                      border-top: 10px solid #FF8C42;
                      border-right: 18px solid #FF8C42;
                      border-bottom: 10px solid #FF8C42;
                      border-left: 18px solid #FF8C42;
                    }
                   \s
                    @media only screen and (max-width: 500px) {
                      .button {
                        width: 100% !important;
                        text-align: center !important;
                      }
                    }
                   \s
                    /* Verification Code Box ------------------------------ */
                   \s
                    .verification-code {
                      width: 100%;
                      margin: 30px 0;
                      padding: 24px;
                      -premailer-width: 100%;
                      -premailer-cellpadding: 0;
                      -premailer-cellspacing: 0;
                      background-color: #F4F4F7;
                      border: 2px solid #3869D4;
                      border-radius: 8px;
                      text-align: center;
                    }
                   \s
                    .verification-code h2 {
                      margin: 0 0 10px 0;
                      color: #3869D4;
                      font-size: 24px;
                      font-weight: bold;
                      letter-spacing: 4px;
                      font-family: monospace;
                    }
                   \s
                    .verification-code p {
                      margin: 0;
                      color: #51545E;
                      font-size: 14px;
                    }
                   \s
                    /* Expiration Warning ------------------------------ */
                   \s
                    .expiration-warning {
                      width: 100%;
                      margin: 20px 0;
                      padding: 16px;
                      background-color: #FFF5F5;
                      border: 1px solid #FED7D7;
                      border-radius: 6px;
                      text-align: center;
                    }
                   \s
                    .expiration-warning p {
                      margin: 0;
                      color: #C53030;
                      font-size: 14px;
                      font-weight: bold;
                    }
                   \s
                    /* Icon Styles ------------------------------ */
                   \s
                    .icon-checkmark {
                      width: 60px;
                      height: 60px;
                      margin: 0 auto 20px auto;
                      background-color: #22BC66;
                      border-radius: 50%;
                      display: flex;
                      align-items: center;
                      justify-content: center;
                    }
                   \s
                    .icon-checkmark::before {
                      content: "✓";
                      color: white;
                      font-size: 30px;
                      font-weight: bold;
                    }
                   \s
                    body {
                      background-color: #F2F4F6;
                      color: #51545E;
                    }
                   \s
                    p {
                      color: #51545E;
                    }
                   \s
                    .email-wrapper {
                      width: 100%;
                      margin: 0;
                      padding: 0;
                      -premailer-width: 100%;
                      -premailer-cellpadding: 0;
                      -premailer-cellspacing: 0;
                      background-color: #F2F4F6;
                    }
                   \s
                    .email-content {
                      width: 100%;
                      margin: 0;
                      padding: 0;
                      -premailer-width: 100%;
                      -premailer-cellpadding: 0;
                      -premailer-cellspacing: 0;
                    }
                    /* Masthead ----------------------- */
                   \s
                    .email-masthead {
                      padding: 25px 0;
                      text-align: center;
                    }
                   \s
                    .email-masthead_logo {
                      width: 94px;
                    }
                   \s
                    .email-masthead_name {
                      font-size: 20px;
                      font-weight: bold;
                      color: #3869D4;
                      text-decoration: none;
                      text-shadow: 0 1px 0 white;
                    }
                    /* Body ------------------------------ */
                   \s
                    .email-body {
                      width: 100%;
                      margin: 0;
                      padding: 0;
                      -premailer-width: 100%;
                      -premailer-cellpadding: 0;
                      -premailer-cellspacing: 0;
                    }
                   \s
                    .email-body_inner {
                      width: 570px;
                      margin: 0 auto;
                      padding: 0;
                      -premailer-width: 570px;
                      -premailer-cellpadding: 0;
                      -premailer-cellspacing: 0;
                      background-color: #FFFFFF;
                    }
                   \s
                    .email-footer {
                      width: 570px;
                      margin: 0 auto;
                      padding: 0;
                      -premailer-width: 570px;
                      -premailer-cellpadding: 0;
                      -premailer-cellspacing: 0;
                      text-align: center;
                    }
                   \s
                    .email-footer p {
                      color: #A8AAAF;
                    }
                   \s
                    .body-action {
                      width: 100%;
                      margin: 30px auto;
                      padding: 0;
                      -premailer-width: 100%;
                      -premailer-cellpadding: 0;
                      -premailer-cellspacing: 0;
                      text-align: center;
                    }
                   \s
                    .body-sub {
                      margin-top: 25px;
                      padding-top: 25px;
                      border-top: 1px solid #EAEAEC;
                    }
                   \s
                    .content-cell {
                      padding: 45px;
                    }
                    /*Media Queries ------------------------------ */
                   \s
                    @media only screen and (max-width: 600px) {
                      .email-body_inner,
                      .email-footer {
                        width: 100% !important;
                      }
                    }
                   \s
                    @media (prefers-color-scheme: dark) {
                      body,
                      .email-body,
                      .email-body_inner,
                      .email-content,
                      .email-wrapper,
                      .email-masthead,
                      .email-footer {
                        background-color: #333333 !important;
                        color: #FFF !important;
                      }
                      p,
                      ul,
                      ol,
                      blockquote,
                      h1,
                      h2,
                      h3,
                      span {
                        color: #FFF !important;
                      }
                      .verification-code,
                      .expiration-warning {
                        background-color: #222 !important;
                      }
                      .email-masthead_name {
                        text-shadow: none !important;
                      }
                    }
                   \s
                    :root {
                      color-scheme: light dark;
                      supported-color-schemes: light dark;
                    }
                    </style>
                    <!--[if mso]>
                    <style type="text/css">
                      .f-fallback  {
                        font-family: Arial, sans-serif;
                      }
                    </style>
                  <![endif]-->
                  </head>
                  <body>
                    <span class="preheader">Please verify your email address to complete your registration with JetBlue Flight Service.</span>
                    <table class="email-wrapper" width="100%" cellpadding="0" cellspacing="0" role="presentation">
                      <tr>
                        <td align="center">
                          <table class="email-content" width="100%" cellpadding="0" cellspacing="0" role="presentation">
                            <tr>
                              <td class="email-masthead">
                                <a href="{{company_url}}" class="f-fallback email-masthead_name">
                                ✈️ JetBlue Flight Service
                              </a>
                              </td>
                            </tr>
                            <!-- Email Body -->
                            <tr>
                              <td class="email-body" width="570" cellpadding="0" cellspacing="0">
                                <table class="email-body_inner" align="center" width="570" cellpadding="0" cellspacing="0" role="presentation">
                                  <!-- Body content -->
                                  <tr>
                                    <td class="content-cell">
                                      <div class="f-fallback">
                                        <h1>Welcome {{user_name}}! 🎉</h1>
                                        <p>Thanks for signing up for <strong>JetBlue Flight Service</strong>! We're excited to have you on board.</p>
                                        <p>To complete your registration and start booking flights, please verify your email address by clicking the button below:</p>
                                       \s
                                        <!-- Action Button -->
                                        <table class="body-action" align="center" width="100%" cellpadding="0" cellspacing="0" role="presentation">
                                          <tr>
                                            <td align="center">
                                              <table width="100%" border="0" cellspacing="0" cellpadding="0" role="presentation">
                                                <tr>
                                                  <td align="center">
                                                    <a href="{{verification_url}}" class="f-fallback button button--green" target="_blank">Verify Email Address</a>
                                                  </td>
                                                </tr>
                                              </table>
                                            </td>
                                          </tr>
                                        </table>
                                       \s
                                        <!-- Alternative Verification Method -->
                                        <p style="margin-top: 30px; text-align: center;">Or enter this verification code manually:</p>
                                       \s
                                        <table class="verification-code" align="center" width="100%" cellpadding="0" cellspacing="0" role="presentation">
                                          <tr>
                                            <td align="center">
                                              <h2 class="f-fallback">{{verification_code}}</h2>
                                              <p class="f-fallback">Enter this code in your application</p>
                                            </td>
                                          </tr>
                                        </table>
                                       \s
                                        <!-- Expiration Warning -->
                                        <table class="expiration-warning" align="center" width="100%" cellpadding="0" cellspacing="0" role="presentation">
                                          <tr>
                                            <td align="center">
                                              <p class="f-fallback">⏰ This verification link expires in {{expiration_time}} minutes</p>
                                            </td>
                                          </tr>
                                        </table>
                                       \s
                                        <h3>What happens after verification?</h3>
                                        <ul>
                                          <li><strong>Search & Book Flights:</strong> Access our comprehensive flight search engine</li>
                                          <li><strong>Manage Bookings:</strong> View, modify, and cancel your reservations</li>
                                          <li><strong>Exclusive Offers:</strong> Receive special deals and promotions</li>
                                          <li><strong>24/7 Support:</strong> Get help whenever you need it</li>
                                        </ul>
                                       \s
                                        <p>If you didn't create an account with us, please ignore this email or <a href="{{support_url}}">contact our support team</a>.</p>
                                       \s
                                        <p>Safe travels!<br>
                                          <strong>The JetBlue Flight Service Team</strong></p>
                                       \s
                                        <!-- Sub copy -->
                                        <table class="body-sub" role="presentation">
                                          <tr>
                                            <td>
                                              <p class="f-fallback sub">
                                                <strong>Trouble clicking the button?</strong>\s
                                                Copy and paste this URL into your web browser:
                                              </p>
                                              <p class="f-fallback sub">
                                                <a href="{{verification_url}}">{{verification_url}}</a>
                                              </p>
                                              <p class="f-fallback sub">
                                                This verification link is valid for {{expiration_time}} minutes from the time this email was sent.
                                              </p>
                                            </td>
                                          </tr>
                                        </table>
                                      </div>
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                            <tr>
                              <td>
                                <table class="email-footer" align="center" width="570" cellpadding="0" cellspacing="0" role="presentation">
                                  <tr>
                                    <td class="content-cell" align="center">
                                      <p class="f-fallback sub align-center">
                                        JetBlue Flight Service<br>
                                        📧 <a href="mailto:support@jetblue-service.com">support@jetblue-service.com</a><br>
                                        🌐 <a href="{{company_url}}">www.jetblue-service.com</a>
                                      </p>
                                      <p class="f-fallback sub align-center">
                                        © {{current_year}} JetBlue Flight Service. All rights reserved.
                                      </p>
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </body>
                </html>
                """;
    }


}
