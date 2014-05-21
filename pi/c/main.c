#include <ph_Status.h>
#include <phhalHw.h>
#include <phOsal.h>
#include <phCardEmu.h>
#include <phCardEmu_Options.h>
#include <phbalReg_RPiSpi.h>

#ifdef  PHCARDEMU_T2T_MSG
#include <CheckNdef.h>
#endif  // PHCARDEMU_T2T_MSG

#include <stdio.h>
#include <string.h>

/* Local macros and definitions */

// size of communication buffers (required for HAL)
#define INPUT_BUFF_MAX     256      // for T4T according to FSCI in phpalI14443p4C.h
#define OUTPUT_BUFF_MAX    256
#define FB_BUF_MAX         2048

#define CHECK_STATUS(status) if (PH_ERR_SUCCESS != status) error (status, __LINE__);

/* Local variables */

// communication buffers for HAL
uint8_t bTxBuff[OUTPUT_BUFF_MAX+1];
uint8_t bRxBuff[INPUT_BUFF_MAX+1];
uint8_t bTimerExpired;

phbalReg_RPi_spi_DataParams_t   bal;            /* BAL component holder */
phhalHw_Pn512_DataParams_t      hal;            /* HAL component holder */
phOsal_RPi_DataParams_t         osal;           /* OSAL component holder for RaspberryPi */

uint8_t bTempBuff[FB_BUF_MAX];

/* Functions */

// gets called in case of error
static void error(phStatus_t status, int line)
    {
    // there was an error

    volatile phStatus_t s;  /* to prevent optimizations */
    volatile int l;         /* to prevent optimizations */

    s = status;
    l = line;

    while (1) {};
    }

void RFAntiColl_TimerCallback(uint32_t timer_id, void * context)
    {
    bTimerExpired = 1;
    return;
    }


/* main */

int main( int argc, char * argv[] )
    {
    uint8_t    * pRxBuffer;
    uint8_t    * chp;
    uint8_t    statusT2T;
    uint8_t    sak;
    uint16_t   wRxLength;
    uint32_t   status_1;
    uint32_t   dwJavaMsgLen;
    uint8_t    bValue;
    phStatus_t status;

    chp = (uint8_t *)dirname(argv[0]);
    chdir(chp);

    /* BEGIN: HW INITIALIZATION */

    /* Initialize the Reader BAL (Bus Abstraction Layer) component */
    status = phbalReg_RPi_spi_Init(&bal, sizeof(phbalReg_RPi_spi_DataParams_t));


    if (PH_ERR_SUCCESS != status)
	{
		printf("Failed to initialize SPI\n");
		return 1;
	}

    /* set up GPIO and SSP port - see underlying functions for more information */
    status = phbalReg_OpenPort(&bal);
    if (PH_ERR_SUCCESS != status)
        {
        printf("Failed to open bal\n");
        return 2;
        }

    /* init HAL for PN512 (PN512 is derivative of PN512) */
    status = phhalHw_Pn512_Init(&hal,
                                sizeof(phhalHw_Pn512_DataParams_t),
                                &bal,
                                NULL,
                                &osal,
                                &bTxBuff[0],
                                OUTPUT_BUFF_MAX+1,
                                &bRxBuff[0],
                                INPUT_BUFF_MAX+1);
    CHECK_STATUS(status);

    /* Set the HAL configuration to SPI */
    status = phhalHw_SetConfig(&hal, PHHAL_HW_CONFIG_BAL_CONNECTION, PHHAL_HW_BAL_CONNECTION_SPI);
    if (PH_ERR_SUCCESS != status)
        {
        printf("Failed to set hal connection SPI\n");
        return 4;
        }

    /* It is necessary to soft-reset the PN512 here. */
    status = phhalHw_Pn512_Cmd_SoftReset(&hal);
    CHECK_STATUS(status);

    /* Initialize the timer component */
    status = phOsal_RPi_Timer_Init(&osal);
    CHECK_STATUS(status);

    /* Initialize OSAL layer */
    status = phOsal_RPi_Init(&osal);
    CHECK_STATUS(status);

    /* END: HW INITIALIZATION */

    /* Card emu. initialization - card emulation is presented like MIFARE Ultralight card */
    sak = 0x00;
    phCardEmu_Init(&hal, &osal, sak);

    /* Initialize the T2T (Type 2-Tag) */
    statusT2T = phCardEmu_T2T_Init();

    /* Set the communication protocol */
    status = phhalHw_ApplyProtocolSettings(&hal, PHHAL_HW_CARDTYPE_ISO14443A_CE);
    CHECK_STATUS(status);

    printf("Connect phone to Pi.\n");


    uint8_t bndefstatus;

    while(1)
        {
        /* Card emulation activation is required */
        if(phCardEmu_Activate(&pRxBuffer, &wRxLength) != PH_ERR_SUCCESS)
            {
            continue;
            }

        if(statusT2T)
            {
            /* T2T proper operations*/
            phCardEmu_T2T_Start(pRxBuffer, wRxLength);

#ifdef  PHCARDEMU_T2T_MSG
            /* Processing the NDEF message and passing it to python script */
            if( CheckNdef( t2tMemory, &NdefDesc ) == NDEF_TEXT_WRITE )
                {
                fflush(stdout);
		char command[80];
		strcpy(command, "python /home/pi/py/picture.py ");
		strcat(command, NdefDesc.textstart);
                // start python script
		system(command);
                }
#endif  // PHCARDEMU_T2T_MSG
            }
        }
    return 0;
    }

