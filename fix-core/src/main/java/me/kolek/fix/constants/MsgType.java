package me.kolek.fix.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum MsgType {
    Heartbeat(MsgType._Heartbeat),
    TestRequest(MsgType._TestRequest),
    ResendRequest(MsgType._ResendRequest),
    Reject(MsgType._Reject),
    SequenceReset(MsgType._SequenceReset),
    Logout(MsgType._Logout),
    IOI(MsgType._IOI),
    Advertisement(MsgType._Advertisement),
    ExecutionReport(MsgType._ExecutionReport),
    OrderCancelReject(MsgType._OrderCancelReject),
    Logon(MsgType._Logon),
    News(MsgType._News),
    Email(MsgType._Email),
    NewOrderSingle(MsgType._NewOrderSingle),
    NewOrderList(MsgType._NewOrderList),
    OrderCancelRequest(MsgType._OrderCancelRequest),
    OrderCancelReplaceRequest(MsgType._OrderCancelReplaceRequest),
    OrderStatusRequest(MsgType._OrderStatusRequest),
    AllocationInstruction(MsgType._AllocationInstruction),
    ListCancelRequest(MsgType._ListCancelRequest),
    ListExecute(MsgType._ListExecute),
    ListStatusRequest(MsgType._ListStatusRequest),
    ListStatus(MsgType._ListStatus),
    AllocationInstructionAck(MsgType._AllocationInstructionAck),
    DontKnowTrade(MsgType._DontKnowTrade),
    QuoteRequest(MsgType._QuoteRequest),
    Quote(MsgType._Quote),
    SettlementInstructions(MsgType._SettlementInstructions),
    MarketDataRequest(MsgType._MarketDataRequest),
    MarketDataSnapshotFullRefresh(MsgType._MarketDataSnapshotFullRefresh),
    MarketDataIncrementalRefresh(MsgType._MarketDataIncrementalRefresh),
    MarketDataRequestReject(MsgType._MarketDataRequestReject),
    QuoteCancel(MsgType._QuoteCancel),
    QuoteStatusRequest(MsgType._QuoteStatusRequest),
    MassQuoteAck(MsgType._MassQuoteAck),
    SecurityDefinitionRequest(MsgType._SecurityDefinitionRequest),
    SecurityDefinition(MsgType._SecurityDefinition),
    SecurityStatusRequest(MsgType._SecurityStatusRequest),
    SecurityStatus(MsgType._SecurityStatus),
    TradingSessionStatusRequest(MsgType._TradingSessionStatusRequest),
    TradingSessionStatus(MsgType._TradingSessionStatus),
    MassQuote(MsgType._MassQuote),
    BusinessMessageReject(MsgType._BusinessMessageReject),
    BidRequest(MsgType._BidRequest),
    BidResponse(MsgType._BidResponse),
    ListStrikePrice(MsgType._ListStrikePrice),
    XMLnonFIX(MsgType._XMLnonFIX),
    RegistrationInstructions(MsgType._RegistrationInstructions),
    RegistrationInstructionsResponse(MsgType._RegistrationInstructionsResponse),
    OrderMassCancelRequest(MsgType._OrderMassCancelRequest),
    OrderMassCancelReport(MsgType._OrderMassCancelReport),
    NewOrderCross(MsgType._NewOrderCross),
    CrossOrderCancelReplaceRequest(MsgType._CrossOrderCancelReplaceRequest),
    CrossOrderCancelRequest(MsgType._CrossOrderCancelRequest),
    SecurityTypeRequest(MsgType._SecurityTypeRequest),
    SecurityTypes(MsgType._SecurityTypes),
    SecurityListRequest(MsgType._SecurityListRequest),
    SecurityList(MsgType._SecurityList),
    DerivativeSecurityListRequest(MsgType._DerivativeSecurityListRequest),
    DerivativeSecurityList(MsgType._DerivativeSecurityList),
    NewOrderMultileg(MsgType._NewOrderMultileg),
    MultilegOrderCancelReplace(MsgType._MultilegOrderCancelReplace),
    TradeCaptureReportRequest(MsgType._TradeCaptureReportRequest),
    TradeCaptureReport(MsgType._TradeCaptureReport),
    OrderMassStatusRequest(MsgType._OrderMassStatusRequest),
    QuoteRequestReject(MsgType._QuoteRequestReject),
    RFQRequest(MsgType._RFQRequest),
    QuoteStatusReport(MsgType._QuoteStatusReport),
    QuoteResponse(MsgType._QuoteResponse),
    Confirmation(MsgType._Confirmation),
    PositionMaintenanceRequest(MsgType._PositionMaintenanceRequest),
    PositionMaintenanceReport(MsgType._PositionMaintenanceReport),
    RequestForPositions(MsgType._RequestForPositions),
    RequestForPositionsAck(MsgType._RequestForPositionsAck),
    PositionReport(MsgType._PositionReport),
    TradeCaptureReportRequestAck(MsgType._TradeCaptureReportRequestAck),
    TradeCaptureReportAck(MsgType._TradeCaptureReportAck),
    AllocationReport(MsgType._AllocationReport),
    AllocationReportAck(MsgType._AllocationReportAck),
    ConfirmationAck(MsgType._ConfirmationAck),
    SettlementInstructionRequest(MsgType._SettlementInstructionRequest),
    AssignmentReport(MsgType._AssignmentReport),
    CollateralRequest(MsgType._CollateralRequest),
    CollateralAssignment(MsgType._CollateralAssignment),
    CollateralResponse(MsgType._CollateralResponse),
    CollateralReport(MsgType._CollateralReport),
    CollateralInquiry(MsgType._CollateralInquiry),
    NetworkCounterpartySystemStatusRequest(MsgType._NetworkCounterpartySystemStatusRequest),
    NetworkCounterpartySystemStatusResponse(MsgType._NetworkCounterpartySystemStatusResponse),
    UserRequest(MsgType._UserRequest),
    UserResponse(MsgType._UserResponse),
    CollateralInquiryAck(MsgType._CollateralInquiryAck),
    ConfirmationRequest(MsgType._ConfirmationRequest),
    ContraryIntentionReport(MsgType._ContraryIntentionReport),
    SecurityDefinitionUpdateReport(MsgType._SecurityDefinitionUpdateReport),
    SecurityListUpdateReport(MsgType._SecurityListUpdateReport),
    AdjustedPositionReport(MsgType._AdjustedPositionReport),
    AllocationInstructionAlert(MsgType._AllocationInstructionAlert),
    ExecutionAck(MsgType._ExecutionAck),
    TradingSessionList(MsgType._TradingSessionList),
    TradingSessionListRequest(MsgType._TradingSessionListRequest),
    SettlementObligationReport(MsgType._SettlementObligationReport),
    DerivativeSecurityListUpdateReport(MsgType._DerivativeSecurityListUpdateReport),
    TradingSessionListUpdateReport(MsgType._TradingSessionListUpdateReport),
    MarketDefinitionRequest(MsgType._MarketDefinitionRequest),
    MarketDefinition(MsgType._MarketDefinition),
    MarketDefinitionUpdateReport(MsgType._MarketDefinitionUpdateReport),
    ApplicationMessageRequest(MsgType._ApplicationMessageRequest),
    ApplicationMessageRequestAck(MsgType._ApplicationMessageRequestAck),
    ApplicationMessageReport(MsgType._ApplicationMessageReport),
    OrderMassActionReport(MsgType._OrderMassActionReport),
    OrderMassActionRequest(MsgType._OrderMassActionRequest),
    UserNotification(MsgType._UserNotification),
    StreamAssignmentRequest(MsgType._StreamAssignmentRequest),
    StreamAssignmentReport(MsgType._StreamAssignmentReport),
    StreamAssignmentReportACK(MsgType._StreamAssignmentReportACK),
    PartyDetailsListRequest(MsgType._PartyDetailsListRequest),
    PartyDetailsListReport(MsgType._PartyDetailsListReport),
    MarginRequirementInquiry(MsgType._MarginRequirementInquiry),
    MarginRequirementInquiryAck(MsgType._MarginRequirementInquiryAck),
    MarginRequirementReport(MsgType._MarginRequirementReport),
    PartyDetailsListUpdateReport(MsgType._PartyDetailsListUpdateReport),
    PartyRiskLimitsRequest(MsgType._PartyRiskLimitsRequest),
    PartyRiskLimitsReport(MsgType._PartyRiskLimitsReport),
    SecurityMassStatusRequest(MsgType._SecurityMassStatusRequest),
    SecurityMassStatus(MsgType._SecurityMassStatus),
    AccountSummaryReport(MsgType._AccountSummaryReport),
    PartyRiskLimitsUpdateReport(MsgType._PartyRiskLimitsUpdateReport),
    PartyRiskLimitsDefinitionRequest(MsgType._PartyRiskLimitsDefinitionRequest),
    PartyRiskLimitsDefinitionRequestAck(MsgType._PartyRiskLimitsDefinitionRequestAck),
    PartyEntitlementsRequest(MsgType._PartyEntitlementsRequest),
    PartyEntitlementsReport(MsgType._PartyEntitlementsReport),
    QuoteAck(MsgType._QuoteAck),
    PartyDetailsDefinitionRequest(MsgType._PartyDetailsDefinitionRequest),
    PartyDetailsDefinitionRequestAck(MsgType._PartyDetailsDefinitionRequestAck),
    PartyEntitlementsUpdateReport(MsgType._PartyEntitlementsUpdateReport),
    PartyEntitlementsDefinitionRequest(MsgType._PartyEntitlementsDefinitionRequest),
    PartyEntitlementsDefinitionRequestAck(MsgType._PartyEntitlementsDefinitionRequestAck),
    TradeMatchReport(MsgType._TradeMatchReport),
    TradeMatchReportAck(MsgType._TradeMatchReportAck),
    PartyRiskLimitsReportAck(MsgType._PartyRiskLimitsReportAck),
    PartyRiskLimitCheckRequest(MsgType._PartyRiskLimitCheckRequest),
    PartyRiskLimitCheckRequestAck(MsgType._PartyRiskLimitCheckRequestAck),
    PartyActionRequest(MsgType._PartyActionRequest),
    PartyActionReport(MsgType._PartyActionReport),
    MassOrder(MsgType._MassOrder),
    MassOrderAck(MsgType._MassOrderAck),
    PositionTransferInstruction(MsgType._PositionTransferInstruction),
    PositionTransferInstructionAck(MsgType._PositionTransferInstructionAck),
    PositionTransferReport(MsgType._PositionTransferReport),
    MarketDataStatisticsRequest(MsgType._MarketDataStatisticsRequest),
    MarketDataStatisticsReport(MsgType._MarketDataStatisticsReport),
    CollateralReportAck(MsgType._CollateralReportAck),
    MarketDataReport(MsgType._MarketDataReport),
    CrossRequest(MsgType._CrossRequest),
    CrossRequestAck(MsgType._CrossRequestAck);

    public static final String _Heartbeat = "0";
    public static final String _TestRequest = "1";
    public static final String _ResendRequest = "2";
    public static final String _Reject = "3";
    public static final String _SequenceReset = "4";
    public static final String _Logout = "5";
    public static final String _IOI = "6";
    public static final String _Advertisement = "7";
    public static final String _ExecutionReport = "8";
    public static final String _OrderCancelReject = "9";
    public static final String _Logon = "A";
    public static final String _News = "B";
    public static final String _Email = "C";
    public static final String _NewOrderSingle = "D";
    public static final String _NewOrderList = "E";
    public static final String _OrderCancelRequest = "F";
    public static final String _OrderCancelReplaceRequest = "G";
    public static final String _OrderStatusRequest = "H";
    public static final String _AllocationInstruction = "J";
    public static final String _ListCancelRequest = "K";
    public static final String _ListExecute = "L";
    public static final String _ListStatusRequest = "M";
    public static final String _ListStatus = "N";
    public static final String _AllocationInstructionAck = "P";
    public static final String _DontKnowTrade = "Q";
    public static final String _QuoteRequest = "R";
    public static final String _Quote = "S";
    public static final String _SettlementInstructions = "T";
    public static final String _MarketDataRequest = "V";
    public static final String _MarketDataSnapshotFullRefresh = "W";
    public static final String _MarketDataIncrementalRefresh = "X";
    public static final String _MarketDataRequestReject = "Y";
    public static final String _QuoteCancel = "Z";
    public static final String _QuoteStatusRequest = "a";
    public static final String _MassQuoteAck = "b";
    public static final String _SecurityDefinitionRequest = "c";
    public static final String _SecurityDefinition = "d";
    public static final String _SecurityStatusRequest = "e";
    public static final String _SecurityStatus = "f";
    public static final String _TradingSessionStatusRequest = "g";
    public static final String _TradingSessionStatus = "h";
    public static final String _MassQuote = "i";
    public static final String _BusinessMessageReject = "j";
    public static final String _BidRequest = "k";
    public static final String _BidResponse = "l";
    public static final String _ListStrikePrice = "m";
    public static final String _XMLnonFIX = "n";
    public static final String _RegistrationInstructions = "o";
    public static final String _RegistrationInstructionsResponse = "p";
    public static final String _OrderMassCancelRequest = "q";
    public static final String _OrderMassCancelReport = "r";
    public static final String _NewOrderCross = "s";
    public static final String _CrossOrderCancelReplaceRequest = "t";
    public static final String _CrossOrderCancelRequest = "u";
    public static final String _SecurityTypeRequest = "v";
    public static final String _SecurityTypes = "w";
    public static final String _SecurityListRequest = "x";
    public static final String _SecurityList = "y";
    public static final String _DerivativeSecurityListRequest = "z";
    public static final String _DerivativeSecurityList = "AA";
    public static final String _NewOrderMultileg = "AB";
    public static final String _MultilegOrderCancelReplace = "AC";
    public static final String _TradeCaptureReportRequest = "AD";
    public static final String _TradeCaptureReport = "AE";
    public static final String _OrderMassStatusRequest = "AF";
    public static final String _QuoteRequestReject = "AG";
    public static final String _RFQRequest = "AH";
    public static final String _QuoteStatusReport = "AI";
    public static final String _QuoteResponse = "AJ";
    public static final String _Confirmation = "AK";
    public static final String _PositionMaintenanceRequest = "AL";
    public static final String _PositionMaintenanceReport = "AM";
    public static final String _RequestForPositions = "AN";
    public static final String _RequestForPositionsAck = "AO";
    public static final String _PositionReport = "AP";
    public static final String _TradeCaptureReportRequestAck = "AQ";
    public static final String _TradeCaptureReportAck = "AR";
    public static final String _AllocationReport = "AS";
    public static final String _AllocationReportAck = "AT";
    public static final String _ConfirmationAck = "AU";
    public static final String _SettlementInstructionRequest = "AV";
    public static final String _AssignmentReport = "AW";
    public static final String _CollateralRequest = "AX";
    public static final String _CollateralAssignment = "AY";
    public static final String _CollateralResponse = "AZ";
    public static final String _CollateralReport = "BA";
    public static final String _CollateralInquiry = "BB";
    public static final String _NetworkCounterpartySystemStatusRequest = "BC";
    public static final String _NetworkCounterpartySystemStatusResponse = "BD";
    public static final String _UserRequest = "BE";
    public static final String _UserResponse = "BF";
    public static final String _CollateralInquiryAck = "BG";
    public static final String _ConfirmationRequest = "BH";
    public static final String _ContraryIntentionReport = "BO";
    public static final String _SecurityDefinitionUpdateReport = "BP";
    public static final String _SecurityListUpdateReport = "BK";
    public static final String _AdjustedPositionReport = "BL";
    public static final String _AllocationInstructionAlert = "BM";
    public static final String _ExecutionAck = "BN";
    public static final String _TradingSessionList = "BJ";
    public static final String _TradingSessionListRequest = "BI";
    public static final String _SettlementObligationReport = "BQ";
    public static final String _DerivativeSecurityListUpdateReport = "BR";
    public static final String _TradingSessionListUpdateReport = "BS";
    public static final String _MarketDefinitionRequest = "BT";
    public static final String _MarketDefinition = "BU";
    public static final String _MarketDefinitionUpdateReport = "BV";
    public static final String _ApplicationMessageRequest = "BW";
    public static final String _ApplicationMessageRequestAck = "BX";
    public static final String _ApplicationMessageReport = "BY";
    public static final String _OrderMassActionReport = "BZ";
    public static final String _OrderMassActionRequest = "CA";
    public static final String _UserNotification = "CB";
    public static final String _StreamAssignmentRequest = "CC";
    public static final String _StreamAssignmentReport = "CD";
    public static final String _StreamAssignmentReportACK = "CE";
    public static final String _PartyDetailsListRequest = "CF";
    public static final String _PartyDetailsListReport = "CG";
    public static final String _MarginRequirementInquiry = "CH";
    public static final String _MarginRequirementInquiryAck = "CI";
    public static final String _MarginRequirementReport = "CJ";
    public static final String _PartyDetailsListUpdateReport = "CK";
    public static final String _PartyRiskLimitsRequest = "CL";
    public static final String _PartyRiskLimitsReport = "CM";
    public static final String _SecurityMassStatusRequest = "CN";
    public static final String _SecurityMassStatus = "CO";
    public static final String _AccountSummaryReport = "CQ";
    public static final String _PartyRiskLimitsUpdateReport = "CR";
    public static final String _PartyRiskLimitsDefinitionRequest = "CS";
    public static final String _PartyRiskLimitsDefinitionRequestAck = "CT";
    public static final String _PartyEntitlementsRequest = "CU";
    public static final String _PartyEntitlementsReport = "CV";
    public static final String _QuoteAck = "CW";
    public static final String _PartyDetailsDefinitionRequest = "CX";
    public static final String _PartyDetailsDefinitionRequestAck = "CY";
    public static final String _PartyEntitlementsUpdateReport = "CZ";
    public static final String _PartyEntitlementsDefinitionRequest = "DA";
    public static final String _PartyEntitlementsDefinitionRequestAck = "DB";
    public static final String _TradeMatchReport = "DC";
    public static final String _TradeMatchReportAck = "DD";
    public static final String _PartyRiskLimitsReportAck = "DE";
    public static final String _PartyRiskLimitCheckRequest = "DF";
    public static final String _PartyRiskLimitCheckRequestAck = "DG";
    public static final String _PartyActionRequest = "DH";
    public static final String _PartyActionReport = "DI";
    public static final String _MassOrder = "DJ";
    public static final String _MassOrderAck = "DK";
    public static final String _PositionTransferInstruction = "DL";
    public static final String _PositionTransferInstructionAck = "DM";
    public static final String _PositionTransferReport = "DN";
    public static final String _MarketDataStatisticsRequest = "DO";
    public static final String _MarketDataStatisticsReport = "DP";
    public static final String _CollateralReportAck = "DQ";
    public static final String _MarketDataReport = "DR";
    public static final String _CrossRequest = "DS";
    public static final String _CrossRequestAck = "DT";

    private final String value;

    MsgType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    private static final Map<String, MsgType> byValue = Collections
            .unmodifiableMap(Arrays.stream(values()).collect(Collectors.toMap(MsgType::value, Function.identity())));

    public static Optional<MsgType> fromValue(String value) {
        return Optional.ofNullable(byValue.get(value));
    }
}
