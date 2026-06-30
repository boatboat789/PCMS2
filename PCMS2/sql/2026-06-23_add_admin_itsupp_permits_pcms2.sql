-- Add ADMIN and ITSUPP roles to PCMS2 Permits table
-- Required for Job Management page access
-- Run on: DEV [] PRD []
SET NOCOUNT ON;

IF NOT EXISTS (
    SELECT 1 FROM [PCMS].[dbo].[Permits]
    WHERE [WebApp] = 'PCMS2' AND [PermitId] = 'ADMIN'
)
BEGIN
    INSERT INTO [PCMS].[dbo].[Permits]
        ([WebApp], [PermitId], [Description],
         [IsPCMSMain], [IsPCMSDetail], [IsPCMSMainToProd], [IsPCMSMainToLBMS],
         [IsPCMSMainToQCMS], [IsPCMSMainToInspect], [IsPCMSMainToSFC],
         [IsReport], [IsUserManagement],
         [DataStatus], [ChangeBy], [ChangeDate], [CreateBy], [CreateDate])
    VALUES
        ('PCMS2', 'ADMIN', 'Administrator',
         1, 1, 1, 1,
         1, 1, 1,
         1, 1,
         'O', 'SYSTEM', GETDATE(), 'SYSTEM', GETDATE())
    PRINT 'ADMIN permit inserted for PCMS2'
END
ELSE
    PRINT 'ADMIN permit already exists for PCMS2'

IF NOT EXISTS (
    SELECT 1 FROM [PCMS].[dbo].[Permits]
    WHERE [WebApp] = 'PCMS2' AND [PermitId] = 'ITSUPP'
)
BEGIN
    INSERT INTO [PCMS].[dbo].[Permits]
        ([WebApp], [PermitId], [Description],
         [IsPCMSMain], [IsPCMSDetail], [IsPCMSMainToProd], [IsPCMSMainToLBMS],
         [IsPCMSMainToQCMS], [IsPCMSMainToInspect], [IsPCMSMainToSFC],
         [IsReport], [IsUserManagement],
         [DataStatus], [ChangeBy], [ChangeDate], [CreateBy], [CreateDate])
    VALUES
        ('PCMS2', 'ITSUPP', 'IT Support',
         1, 1, 1, 1,
         1, 1, 1,
         1, 1,
         'O', 'SYSTEM', GETDATE(), 'SYSTEM', GETDATE())
    PRINT 'ITSUPP permit inserted for PCMS2'
END
ELSE
    PRINT 'ITSUPP permit already exists for PCMS2'
